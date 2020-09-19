package com.space.service.impl;

import com.space.controller.ShipOrder;
import com.space.exception.DBConstraintException;
import com.space.exception.DBDataException;
import com.space.exception.NotFoundException;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import com.space.specification.ShipSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {

    private static final int RATING_CONSTANT = 80;
    private static final int PLANET_NAME_MAXIMUM = 50;
    private static final int SHIP_NAME_MAXIMUM = 50;
    private static final double SPEED_MINIMUM = 0.01;
    private static final double SPEED_MAXIMUM = 0.99;
    private static final int CREW_MINIMUM = 1;
    private static final int CREW_MAXIMUM = 9999;
    private static final int YEAR_MINIMUM = 2800;
    private static final int YEAR_MAXIMUM = 3019;
    private static final int CURRENT_YEAR = 3019;
    private static final double USED_COEFFICIENT = 0.5;
    private static final double NEW_COEFFICIENT = 1.0;
    private final ShipRepository shipRepository;

    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Optional<Ship> getShip(Long id) {
        checkIdPositive(id);
        return shipRepository.findById(id);
    }

    @Override
    public void deleteShip(Long id) {
        checkIdPositive(id);
        if (!shipRepository.existsById(id))
            throw new NotFoundException();
        shipRepository.deleteById(id);
    }

    @Override
    public Ship createShip(Ship ship) {
        checkFields(ship);
        checkConstraints(ship);
        calculateRating(ship);
        return shipRepository.save(ship);
    }

    @Override
    public Ship updateShip(Long id, Ship ship) {
        checkIdPositive(id);
        Ship existingShip = shipRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mergeShips(existingShip, ship);
        checkFields(ship);
        checkConstraints(ship);
        calculateRating(ship);
        return shipRepository.save(ship);
    }

    @Override
    public List<Ship> getShips(Map<String, String> paramList) {
        int pageNumber = Integer.parseInt(paramList.get("pageNumber"));
        int pageSize = Integer.parseInt(paramList.get("pageSize"));
        Sort sort = Sort.by("id");
        if (paramList.containsKey("order")) {
            sort = Sort.by(ShipOrder.valueOf(paramList.get("order")).getFieldName());
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Ship> page = shipRepository.findAll(new ShipSpecification(paramList), pageable);
        return page.getContent();
    }

    @Override
    public Integer getCount(Map<String, String> paramsList) {
        return shipRepository.findAll(new ShipSpecification(paramsList)).size();
    }

    private void calculateRating(Ship ship) {
        double k = ship.getUsed() ? USED_COEFFICIENT : NEW_COEFFICIENT;
        int prodDate =
                ship.getProdDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        Double rating = new BigDecimal(RATING_CONSTANT * ship.getSpeed() * k /
                (CURRENT_YEAR - prodDate + 1))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        ship.setRating(rating);
    }

    private void checkIdPositive(Long id) {
        if (id <= 0)
            throw new DBDataException();
    }

    private void checkConstraints(Ship ship) {
        if (ship.getPlanet().length() > PLANET_NAME_MAXIMUM
                || ship.getName().length() > SHIP_NAME_MAXIMUM)
            throw new DBConstraintException();
    }

    private void checkFields(Ship ship) {
        int year = ship.getProdDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        if (ship.getName().isEmpty()
                || ship.getPlanet().isEmpty()
                || ship.getSpeed() < SPEED_MINIMUM
                || ship.getSpeed() > SPEED_MAXIMUM
                || ship.getCrewSize() < CREW_MINIMUM
                || ship.getCrewSize() > CREW_MAXIMUM
                || year < YEAR_MINIMUM
                || year > YEAR_MAXIMUM)
            throw new DBDataException();
    }

    private void mergeShips(Ship from, Ship to) {
        to.setId(from.getId());
        if (to.getPlanet() == null)
            to.setPlanet(from.getPlanet());
        if (to.getName() == null)
            to.setName(from.getName());
        if (to.getShipType() == null)
            to.setShipType(from.getShipType());
        if (to.getProdDate() == null)
            to.setProdDate(from.getProdDate());
        if (to.getUsed() == null)
            to.setUsed(from.getUsed());
        if (to.getSpeed() == null)
            to.setSpeed(from.getSpeed());
        if (to.getCrewSize() == null)
            to.setCrewSize(from.getCrewSize());
    }
}