package com.samuel_leong.blokus.repository;

import com.samuel_leong.blokus.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {

}
