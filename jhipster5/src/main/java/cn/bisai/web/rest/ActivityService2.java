package cn.bisai.web.rest;

import cn.bisai.domain.Film;
import cn.bisai.domain.Vedio;
import cn.bisai.repository.FilmRepository;
import cn.bisai.repository.VedioRepository;
import cn.bisai.web.websocket.dto.ActivityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.Instant;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static cn.bisai.config.WebsocketConfiguration.IP_ADDRESS;

@RestController
public class ActivityService2 implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService2.class);

    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private  VedioRepository vedioRepository;
    @Autowired
    private FilmRepository filmRepository;



    public ActivityService2(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

   // @MessageMapping("/topic/activity")
   // @SendTo("/topic/tracker")
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
    //@MessageMapping("/setplay/{id}")
    public Film setplay(@PathVariable Long id){
        Optional<Film> opt=filmRepository.findById(id);
        Film f=opt.get();
        if(f.isPlaying()){
            log.info("视频已经在播放了,进度是"+f.getCurtime());
        }else{
            f.setPlaying(true);
            log.info("从"+f.getCurtime()+"开始播放");
        }
        return f;
    }
    //@MessageMapping("/setstop/id")
    public Film setstop(@PathVariable Long id){
        Optional<Film> opt=filmRepository.findById(id);
        Film f=opt.get();
        if(f.isPlaying()){
            f.setPlaying(false);
            log.info("视频已经在停止了,进度是"+f.getCurtime());
        }else{
            //f.setPlaying(true);
            log.info("播放到"+f.getCurtime());
        }
        return f;
    }
    //@MessageMapping("/switch/{id}")
    public Film switchvedio(Long id){
        Optional<Film> opt=filmRepository.findById(id);
        Film f=opt.get();
        if(f.isPlaying()){
            log.info("视频已经在播放了,进度是"+f.getCurtime());
        }else{
            f.setPlaying(true);
            log.info("从"+f.getCurtime()+"开始播放");
        }
        return f;
    }
    //@MessageMapping("/notice/{id}")
    //@Scheduled(cron="0/5****?")
    public void notice(@PathVariable Long id){
        Optional<Film> opt=filmRepository.findById(id);
        Film f=opt.get();
        Timer timer=new Timer();
        long delay=0;
        long internal=5*1000;
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                if(f.isPlaying()){
                    if(f.getCurtime()<=f.getLength()){
                        log.info("播放到了"+f.getCurtime());
                        f.setCurtime(f.getCurtime()+5);
                        filmRepository.updateCurtime(id,f.getCurtime());
                    }else{
                        f.setPlaying(false);
                        log.info("播放结束");
                    }

                }
            }
        };
        timer.schedule(task,delay,internal);


    }
   // @MessageMapping("/skipfront/{id}")
    //@Scheduled(cron="*****?")
    public void skipfront(@PathVariable Long id){


    }
    public void skipback(){

    }
}
