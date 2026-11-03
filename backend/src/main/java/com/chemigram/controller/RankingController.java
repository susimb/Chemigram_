
package com.chemigram.controller;

import com.chemigram.dto.UsuarioResponse;
import com.chemigram.service.RankingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerRanking() {
        List<UsuarioResponse> ranking =
                rankingService.obtenerRanking();

        return ResponseEntity.ok(ranking);
    }
}
