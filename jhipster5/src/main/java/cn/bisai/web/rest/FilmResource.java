package cn.bisai.web.rest;

import cn.bisai.domain.Film;
import cn.bisai.repository.FilmRepository;
import cn.bisai.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.net.dns.ResolverConfiguration;

import javax.validation.Valid;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;

/**
 * REST controller for managing {@link cn.bisai.domain.Film}.
 */
//这是修改版本
@RestController
@RequestMapping("/api")
public class FilmResource {

    private final Logger log = LoggerFactory.getLogger(FilmResource.class);

    private static final String ENTITY_NAME = "film";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilmRepository filmRepository;
    //当前屏幕上的视频
    private Film film = new Film();
    private Date begintime;//=new Date();

    public FilmResource(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    /**
     * {@code POST  /films} : Create a new film.
     *
     * @param film the film to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new film, or with status {@code 400 (Bad Request)} if the film has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/films")
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) throws URISyntaxException {
        log.debug("REST request to save Film : {}", film);
        if (film.getId() != null) {
            throw new BadRequestAlertException("A new film cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Film result = filmRepository.save(film);
        return ResponseEntity.created(new URI("/api/films/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /films} : Updates an existing film.
     *
     * @param film the film to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated film,
     * or with status {@code 400 (Bad Request)} if the film is not valid,
     * or with status {@code 500 (Internal Server Error)} if the film couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws URISyntaxException {
        log.debug("REST request to update Film : {}", film);
        if (film.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Film result = filmRepository.save(film);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, film.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /films} : get all the films.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of films in body.
     */
    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.debug("REST request to get all Films");
        return filmRepository.findAll();
    }

    /**
     * {@code GET  /films/:id} : get the "id" film.
     *
     * @param id the id of the film to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the film, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/films/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable Long id) {
        log.debug("REST request to get Film : {}", id);
        Optional<Film> film = filmRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(film);
    }

    /**
     * {@code DELETE  /films/:id} : delete the "id" film.
     *
     * @param id the id of the film to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/films/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        log.debug("REST request to delete Film : {}", id);
        Optional<Film> opt = filmRepository.findById(id);
        Film f = opt.get();
        File file = new File(f.getUrl());
        if (!file.exists()) {
            log.info("文件不存在");

        } else {
            if (file.delete()) {
                log.info("yijingshanchu");
            } else {
                log.info("删除失败");
            }
        }
        filmRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @RequestMapping("/upload")
    public Film upload(@RequestParam("film") MultipartFile file) {
        log.info("上传视频");
        Film film = new Film();
        String name = file.getOriginalFilename();
        String filepath = "D://vedio//";
        File dest = new File(filepath + name);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        film.setName(name);
        film.setPlaying(false);
        film.setLength(Long.parseLong("5000"));
        film.setCurtime(Long.parseLong("0"));
        filmRepository.save(film);
        return film;
    }

    @RequestMapping("/addfilm")
    public Film addfilm(@RequestParam("name") String name, @RequestParam("length") Long length) {
        Film f = new Film();
        f.setName(name);
        f.setLength(length);
        f.setCurtime(Long.parseLong("0"));
        f.setPlaying(false);
        f.setUrl("D://vedio/" + name);
        filmRepository.save(f);
        return f;
    }

    //@RequestMapping("/setplay/{id}")
    public Film setplay(@PathVariable Long id) {
        Optional<Film> opt = filmRepository.findById(id);
        Film f = opt.get();

        if (f.isPlaying()) {
            log.info("视频已经在播放了,进度是" + f.getCurtime());

        } else {
            f.setPlaying(true);
            filmRepository.updatePlay(id, true);
            log.info("从" + film.getCurtime() + "开始播放");

        }
        Timer timer = new Timer();
        long delay = 0;
        long internal = 1000;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (f.isPlaying()) {
                    f.setCurtime(film.getCurtime() + 1);
                    filmRepository.updateCurtime(f.getId(), f.getCurtime());
                }

            }
        };
        timer.schedule(task, delay, internal);
        return f;
    }

    @RequestMapping("/switch/{id}")
    public Film switchVedio(@PathVariable Long id) {
        filmRepository.updateCurtime(film.getId(), film.getCurtime());
        filmRepository.updatePlay(film.getId(), film.isPlaying());
        Optional<Film> opt = filmRepository.findById(id);
        if(!opt.isPresent()){
            log.info("id错误，再选一个");
            return null;
        }
        film = opt.get();
        begintime = new Date();
        begintime.setTime(System.currentTimeMillis() - film.getCurtime());
        log.info(film.getName() + "视频开始播放时间为" + begintime);
        //schedaltask2();
        pushProgress();
        return film;
    }

    @RequestMapping("/setstop/{id}")
    public Film setStop(@PathVariable Long id) {
        Long fid = film.getId();
        if (!id.equals(fid)) {
            Optional<Film> opt = filmRepository.findById(id);
            if(!opt.isPresent()){
                log.info("id错误，再选一个");
                return null;
            }
            Film f = opt.get();
            f.setPlaying(false);
            filmRepository.updatePlay(f.getId(), false);
            log.info(f.getName() + "已经停止了，到了" + f.getCurtime());
            return f;
        } else {
            if (film.isPlaying()) {
                film.setPlaying(false);
                film.setCurtime(System.currentTimeMillis() - begintime.getTime());
                //filmRepository.updatePlay(film.getId(),false);
                //film.setCurtime();
                log.info(film.getName() + "视频已经在停止了,进度是" + film.getCurtime() / 1000 + "秒" + film.getCurtime() % 1000 + "毫秒");
            } else {
                //f.setPlaying(true);
                log.info(film.getName() + "播放到" + film.getCurtime() / 1000 + "秒" + film.getCurtime() % 1000 + "毫秒");
            }
            return film;
        }


    }

    @RequestMapping("/skiptofront/{id}")
    public void skiptofront(@PathVariable Long id,@RequestParam Long change) {
        Long fid = film.getId();
        if (!id.equals(fid)) {
            Optional<Film> opt = filmRepository.findById(id);
            if(!opt.isPresent()){
                log.info("id错误，再选一个");
                return;
            }
            Film f = opt.get();
            f.setCurtime(f.getCurtime() + change);
            log.info(f.getName() + "快进到" + f.getCurtime());
            filmRepository.updateCurtime(f.getId(), f.getCurtime());
        } else {
            log.info("当前视频快进");
            film.setCurtime(film.getCurtime() + change);
            begintime=new Date();
            begintime.setTime(System.currentTimeMillis() - film.getCurtime());
            log.info(film.getName() + "快进到了" + film.getCurtime() / 1000 + "秒" + film.getCurtime() % 1000);
        }

        //filmRepository.updateCurtime(id,f.getCurtime()+10);
    }

    @RequestMapping("/skiptoback/{id}")
    public void skipToBack(@PathVariable Long id,@RequestParam Long backward) {
        Long fid = film.getId();
        if (!id.equals(fid) || film == null) {
            Optional<Film> opt = filmRepository.findById(id);
            if(!opt.isPresent()){
                log.info("id错误，再选一个");
                return;
            }
            Film f = opt.get();
            if(f.getCurtime().compareTo(backward)==-1||f.getCurtime().compareTo(backward)==0){
                f.setCurtime(Integer.toUnsignedLong(0));
            }else{
                f.setCurtime(f.getCurtime() - backward);
            }

            log.info(f.getName() + "退后到" + f.getCurtime());
            filmRepository.updateCurtime(f.getId(), f.getCurtime());
        } else {
            log.info("当前视频后退");
            if(film.getCurtime().compareTo(backward)==-1||
                film.getCurtime().compareTo(backward)==0){
                film.setCurtime(Integer.toUnsignedLong(0));
            }else{
                film.setCurtime(film.getCurtime() - backward);
            }
            begintime=new Date();
            begintime.setTime(System.currentTimeMillis() - film.getCurtime());
            log.info(film.getName() + "退后到了" + film.getCurtime() / 1000 + "秒" + film.getCurtime() % 1000);
        }
        //filmRepository.updateCurtime(id,f.getCurtime()-10);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void pushProgress() {
        if(film==null){
            //log.info("请选择视频再来播放");
            //throw new RuntimeException("请选择视频再来播放");
            return;
        }else{
            log.info("_" + begintime);
            if (film.isPlaying()) {
                if (film.getCurtime() <= film.getLength()) {
                    log.info(film.getName() + "当前播放到了" + film.getCurtime());
                    film.setCurtime(System.currentTimeMillis() - begintime.getTime());
                    log.info(film.getName() + "正在进度到了" + film.getCurtime() / 1000 + "秒" + film.getCurtime() % 1000 + "毫秒");
                } else {
                    film.setPlaying(false);
                    film.setCurtime(film.getLength());
                    filmRepository.updatePlay(film.getId(), false);
                    log.info(film.getName() + "已经结束了");
                }

            } else {
                log.info(film.getName() + "播放已经暂停,当前进度是：" + film.getCurtime() / 1000 + "秒" + film.getCurtime() % 1000 + "毫秒");
            }
        }



    }

    @RequestMapping("/setplay/{id}")
    public Film setPlay2(@PathVariable Long id) {
        Long fid = film.getId();
        if (!id.equals(fid)) {
            Optional<Film> opt = filmRepository.findById(id);
            if(!opt.isPresent()){
                log.info("id错误，再选一个");
                return null;
            }
            Film f = opt.get();
            f.setPlaying(true);
            filmRepository.updatePlay(f.getId(), true);
            log.info(f.getName() + "已经播放了，到了" + f.getCurtime());
            return f;
        } else {
            if (film.isPlaying()) {
                log.info("视频已经在播放了,进度是" + film.getCurtime());
                log.info(film.getName() + "视频播放开始时间为" + begintime);
            } else {
                film.setPlaying(true);
                begintime = new Date();
                begintime.setTime(System.currentTimeMillis() - film.getCurtime());
                //filmRepository.updatePlay(film.getId(),true);
                log.info(film.getName() + "视频播放开始时间为" + begintime);
                log.info("从" + film.getCurtime() + "开始播放");
            }
            return film;
        }


    }

    //@Scheduled(cron = "0/1 * * * * *")
    public void schedaltask2() {
        if (film.isPlaying()) {
            film.setCurtime(System.currentTimeMillis() - begintime.getTime());
        }
    }


}
