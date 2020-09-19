package com.space.controller;

import com.space.exception.DBConstraintException;
import com.space.exception.DBDataException;
import com.space.exception.NotFoundException;
import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/ships")
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping(produces = "application/json")
    public List<Ship> getShips(
            @RequestParam Map<String, String> customQuery
    ) {
        customQuery.putIfAbsent("pageSize", "3");
        customQuery.putIfAbsent("pageNumber", "0");
        return shipService.getShips(customQuery);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Ship> getById(@PathVariable("id") Long id) {
        Optional<Ship> ship = shipService.getShip(id);
        return ship.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        if (ship.getUsed() == null)
            ship.setUsed(false);
        return new ResponseEntity<>(shipService.createShip(ship), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        shipService.deleteShip(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = {"/{id}"})
    public ResponseEntity<Ship> updateShip(@PathVariable("id") Long id,
                                           @RequestBody Ship ship) {
        return new ResponseEntity<>(shipService.updateShip(id, ship),
                HttpStatus.OK);
    }

    @GetMapping("/count")
    public int getCount(
            @RequestParam Map<String, String> customQuery
    ) {
        return shipService.getCount(customQuery);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NullPointerException.class,
            DBConstraintException.class,
            DBDataException.class})
    public void badRequestHandler() {

    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public void notFoundHandler() {

    }
}
