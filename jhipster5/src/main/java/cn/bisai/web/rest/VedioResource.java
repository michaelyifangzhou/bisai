package cn.bisai.web.rest;

import cn.bisai.domain.Vedio;
import cn.bisai.repository.VedioRepository;
import cn.bisai.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link cn.bisai.domain.Vedio}.
 */
@RestController
@RequestMapping("/api")
public class VedioResource {

    private final Logger log = LoggerFactory.getLogger(VedioResource.class);

    private static final String ENTITY_NAME = "vedio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VedioRepository vedioRepository;

    public VedioResource(VedioRepository vedioRepository) {
        this.vedioRepository = vedioRepository;
    }

    /**
     * {@code POST  /vedios} : Create a new vedio.
     *
     * @param vedio the vedio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vedio, or with status {@code 400 (Bad Request)} if the vedio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vedios")
    public ResponseEntity<Vedio> createVedio(@RequestBody Vedio vedio) throws URISyntaxException {
        log.debug("REST request to save Vedio : {}", vedio);
        if (vedio.getId() != null) {
            throw new BadRequestAlertException("A new vedio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vedio result = vedioRepository.save(vedio);
        return ResponseEntity.created(new URI("/api/vedios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vedios} : Updates an existing vedio.
     *
     * @param vedio the vedio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vedio,
     * or with status {@code 400 (Bad Request)} if the vedio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vedio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vedios")
    public ResponseEntity<Vedio> updateVedio(@RequestBody Vedio vedio,@RequestParam("vedio") MultipartFile file) throws URISyntaxException {
        log.debug("REST request to update Vedio : {}", vedio);
        if (vedio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String name=file.getOriginalFilename();
        String filepath="D://vedio//";
        File dest=new File(filepath+name);
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try{
            file.transferTo(dest);
        }catch (Exception e){
            e.printStackTrace();
        }
        Vedio result = vedioRepository.save(vedio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vedio.getId().toString()))
            .body(result);
    }
   // @RequestMapping("/upload")
    public Vedio upLoadvedio(@RequestParam("vedio") MultipartFile file, HttpServletRequest request){
       Vedio vedio=new Vedio();

        String name=file.getOriginalFilename();
        String filepath="D://vedio//";
        File dest=new File(filepath+name);
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try{
            file.transferTo(dest);
        }catch (Exception e){
            e.printStackTrace();
        }
        vedio.setName(name);
        vedio.setIsplaying(false);
        vedio.setUrl(filepath+name);
        vedioRepository.save(vedio);
        return vedio;
    }
    @RequestMapping("/addvedio")
    public Vedio addVedio(@RequestParam("name")String name,@RequestParam("length") Long length){
        Vedio vedio=new Vedio();
        vedio.setName(name);
        vedio.setLength(length);
        vedio.setUrl("D://vedio/"+name);
        vedio.setIsplaying(false);
        vedioRepository.save(vedio);
        return vedio;
    }
    /**
     * {@code GET  /vedios} : get all the vedios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vedios in body.
     */
    @GetMapping("/vedios")
    public List<Vedio> getAllVedios() {
        log.debug("REST request to get all Vedios");
        return vedioRepository.findAll();
    }

    /**
     * {@code GET  /vedios/:id} : get the "id" vedio.
     *
     * @param id the id of the vedio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vedio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vedios/{id}")
    public ResponseEntity<Vedio> getVedio(@PathVariable Long id) {
        log.debug("REST request to get Vedio : {}", id);
        Optional<Vedio> vedio = vedioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vedio);
    }

    /**
     * {@code DELETE  /vedios/:id} : delete the "id" vedio.
     *
     * @param id the id of the vedio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vedios/{id}")
    public ResponseEntity<Void> deleteVedio(@PathVariable Long id) {
        log.debug("REST request to delete Vedio : {}", id);
        Optional<Vedio> opt=vedioRepository.findById(id);
        Vedio vedio=opt.get();
        File file=new File(vedio.getUrl());
        if(!file.exists()){
            throw new RuntimeException("文件不存在");

        }else{
            if(file.delete()){
                log.info("yijingshanchu");
            }else{
                log.info("删除失败");
            }
        }
        vedioRepository.deleteById(id);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
