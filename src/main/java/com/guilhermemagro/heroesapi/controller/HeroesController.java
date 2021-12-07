package com.guilhermemagro.heroesapi.controller;

import com.guilhermemagro.heroesapi.document.Heroes;
import com.guilhermemagro.heroesapi.repository.HeroesRepository;
import com.guilhermemagro.heroesapi.service.HeroesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.guilhermemagro.heroesapi.constants.HeroesConstants.HEROES_ENDPOINT_LOCAL;

@RestController
@Slf4j
public class HeroesController {

    private HeroesService heroesService;
    private HeroesRepository heroesRepository;

    public static final Logger log = LoggerFactory.getLogger(HeroesController.class);

    public HeroesController(HeroesService heroesService, HeroesRepository heroesRepository) {
        this.heroesService = heroesService;
        this.heroesRepository = heroesRepository;
    }

    @GetMapping(HEROES_ENDPOINT_LOCAL)
    public Flux<Heroes> getAllItems() {
        log.info("Requesting the list of all heroes");
        return heroesService.findAll();
    }

    @GetMapping(HEROES_ENDPOINT_LOCAL + "/id")
    public Mono<ResponseEntity<Heroes>> findByIdHero(@PathVariable String id) {
        log.info("Requesting the hero with id {}", id);
        return heroesService.findByIdHero(id)
                .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(HEROES_ENDPOINT_LOCAL)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Heroes> createHero(@RequestBody Heroes heroes) {
        log.info("A new Hero was created");
        return heroesService.save(heroes);
    }

    @DeleteMapping(HEROES_ENDPOINT_LOCAL + "/id")
    @ResponseStatus(code = HttpStatus.CONTINUE)
    public Mono<HttpStatus> deleteByIdHero(@PathVariable String id) {
        heroesService.deleteByIdHero(id);
        log.info("Deleting a hero with id {}", id);
        return Mono.just(HttpStatus.CONTINUE);
    }
}
