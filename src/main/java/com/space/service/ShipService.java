package com.space.service;

import com.space.model.Ship;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ShipService {
    Optional<Ship> getShip(Long id);
    void deleteShip(Long id);
    Ship createShip(Ship ship);
    Ship updateShip(Long id, Ship ship);
    List<Ship> getShips(Map<String, String> paramList);
    Integer getCount(Map<String, String> paramsList);
}
