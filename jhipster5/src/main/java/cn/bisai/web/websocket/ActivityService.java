package cn.bisai.web.websocket;

import static cn.bisai.config.WebsocketConfiguration.IP_ADDRESS;

import cn.bisai.domain.Film;
import cn.bisai.domain.Film_;
import cn.bisai.domain.Vedio;
import cn.bisai.repository.FilmRepository;
import cn.bisai.repository.VedioRepository;
import cn.bisai.web.websocket.dto.ActivityDTO;

import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class    ActivityService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private  VedioRepository vedioRepository;
    @Autowired
    private FilmRepository filmRepository;

    private Film film=new Film();
    private Date begintime;


    public ActivityService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/topic/activity")
    @SendTo("/topic/tracker")
    public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        activityDTO.setUserLogin(principal.getName());
        activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
        activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
        activityDTO.setTime(Instant.now());
        log.debug("Sending user tracking data {}", activityDTO);
        return activityDTO;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setSessionId(event.getSessionId());
        activityDTO.setPage("logout");
        messagingTemplate.convertAndSend("/topic/tracker", activityDTO);
    }
    @MessageMapping("/setplay/{id}")
    public Film setplay(@PathVariable Long id){
        Long fid=film.getId();
        if(!id.equals(fid)){
            Optional<Film> opt=filmRepository.findById(id);
            if(!opt.isPresent()){
                messagingTemplate.convertAndSend("/topic/user","id错误，再选一个");
                return null;
            }
            Film f=opt.get();
            f.setPlaying(true);
            filmRepository.updatePlay(f.getId(),true);
            log.info(f.getName()+"已经播放了，到了"+f.getCurtime());
        }
        if(film.isPlaying()){
            log.info("视频已经在播放了,进度是"+film.getCurtime());
            log.info(film.getName()+"视频播放开始时间为"+begintime);
        }else{
            film.setPlaying(true);
            begintime=new Date();
            begintime.setTime(System.currentTimeMillis()-film.getCurtime());
            //filmRepository.updatePlay(film.getId(),true);
            messagingTemplate.convertAndSend("/topiv/user",film);
            //log.info(film.getName()+"视频播放开始时间为"+begintime);
            messagingTemplate.convertAndSend("/topiv/user","从"+film.getCurtime()+"开始播放");
            log.info("从"+film.getCurtime()+"开始播放");
        }
        return film;
    }
    @MessageMapping("/setstop/id")
    public Film setstop(@PathVariable Long id){
        Long fid=film.getId();
        if(!id.equals(fid)){
            Optional<Film> opt=filmRepository.findById(id);
            if(!opt.isPresent()){
                messagingTemplate.convertAndSend("/topic/user","id错误，再选一个");
                return null;
            }
            Film f=opt.get();
            f.setPlaying(false);
            filmRepository.updatePlay(f.getId(),false);
            log.info(f.getName()+"已经停止了，到了"+f.getCurtime());
        }else{
            if(film.isPlaying()){
                film.setPlaying(false);
                film.setCurtime(System.currentTimeMillis()-begintime.getTime());
                //filmRepository.updatePlay(film.getId(),false);
                //film.setCurtime();
                log.info(film.getName()+"视频已经在停止了,进度是"+film.getCurtime()/1000+"秒"+film.getCurtime()%1000+"毫秒");
            }else{
                //f.setPlaying(true);
                log.info(film.getName()+"播放到"+film.getCurtime()/1000+"秒"+film.getCurtime()%1000+"毫秒");
            }
        }

        return film;
    }
    @MessageMapping("/switch/{id}")
    public Film switchvedio(@PathVariable Long id){
        filmRepository.updateCurtime(film.getId(),film.getCurtime());
        filmRepository.updatePlay(film.getId(),film.isPlaying());
        Optional<Film> opt=filmRepository.findById(id);
        if(!opt.isPresent()){
            messagingTemplate.convertAndSend("/topic/user","id错误，再选一个");
            return null;
        }
        film=opt.get();
        begintime=new Date();
        begintime.setTime(System.currentTimeMillis()-film.getCurtime());
        log.info(film.getName()+"视频开始播放时间为"+begintime);
        messagingTemplate.convertAndSend("/topic/user",film);
        //schedaltask2();
        scheduledTask();
        return film;
    }


   // @MessageMapping("/skiptoback/{id}")
    //@Scheduled(cron="*****?")
    public void skipfront(@PathVariable Long id){
        Long fid=film.getId();
        if(!id.equals(fid)){
            Optional<Film> opt=filmRepository.findById(id);
            if(!opt.isPresent()){
                messagingTemplate.convertAndSend("/topic/user","id错误，再选一个");
                return;
            }
            Film f=opt.get();
            f.setCurtime(f.getCurtime()-10000);
            begintime = new Date();
            begintime.setTime(System.currentTimeMillis() - film.getCurtime());
            //messagingTemplate.convertAndSend("/topiv/user",f);
            log.info(f.getName()+"退后到"+f.getCurtime());
            filmRepository.updateCurtime(f.getId(),f.getCurtime());
        }else{
            log.info("当前视频后退");
            film.setCurtime(film.getCurtime()-10000);
            messagingTemplate.convertAndSend("/topiv/user",film);
            //log.info(film.getName()+"退后到了"+film.getCurtime()/1000+"秒"+film.getCurtime()%1000);
        }
    }
    @MessageMapping("/changeProgress/{id}")
    public void changeProgress(@PathVariable Long id,Long change){
        Long fid=film.getId();
        if(!id.equals(fid)){
            Optional<Film> opt=filmRepository.findById(id);
            if(!opt.isPresent()){
                messagingTemplate.convertAndSend("/topic/user","id错误，再选一个");
                return;
            }
            Film f=opt.get();
            f.setCurtime(f.getCurtime()+change);
            log.info(f.getName()+"快进到"+f.getCurtime());
            //messagingTemplate.convertAndSend("/topiv/user",f);
            filmRepository.updateCurtime(f.getId(),f.getCurtime());
        }else{
            log.info("正在播放的视频改变进度");
            film.setCurtime(film.getCurtime()+change);
            begintime=new Date();
            begintime.setTime(System.currentTimeMillis()-film.getCurtime());
            messagingTemplate.convertAndSend("/topiv/user",film);
            //log.info(film.getName()+"快进到了"+film.getCurtime()/1000+"秒"+film.getCurtime()%1000);
        }
    }
    @Scheduled(cron = "0/5 * * * * *")
    public void scheduledTask() {
        if(film==null){
            log.info("请选择视频再来播放");
            messagingTemplate.convertAndSend("/topic/user","选择视频再来播放");
        }else{
            log.info("_"+begintime);
            if(film.isPlaying()){
                if(film.getCurtime()<=film.getLength()){
                    log.info(film.getName()+"当前播放到了"+film.getCurtime());
                    film.setCurtime(System.currentTimeMillis()-begintime.getTime());
                    messagingTemplate.convertAndSend("/topic/user",
                        film.getName()+"正在进度到了"+film.getCurtime()/1000+"秒"+film.getCurtime()%1000+"毫秒");
                    //log.info(film.getName()+"正在进度到了"+film.getCurtime()/1000+"秒"+film.getCurtime()%1000+"毫秒");
                }else{
                    film.setPlaying(false);
                    filmRepository.updatePlay(film.getId(),false);
                    //log.info(film.getName()+"已经结束了");
                    messagingTemplate.convertAndSend("/topic/user",
                        film.getName()+"已经结束了");
                }

            }else{
                //log.info(film.getName()+"播放已经暂停,当前进度是："+film.getCurtime()/1000+"秒"+film.getCurtime()%1000+"毫秒");
                messagingTemplate.convertAndSend("/topic/user",
                    film.getName()+"播放已经暂停,当前进度是："+
                        film.getCurtime()/1000+"秒"+film.getCurtime()%1000+"毫秒");
            }

        }



    }

    //@Scheduled(cron = "0/1 * * * * *")
    public void schedaltask2(){
        if(film.isPlaying()){
            film.setCurtime(System.currentTimeMillis()-begintime.getTime());
        }
    }

    @SubscribeMapping("/topic/user")
    public void subscrive(){
        messagingTemplate.convertAndSend("/topic/user","订阅已经成功了，" +
            "将受到消息");
    }
}
