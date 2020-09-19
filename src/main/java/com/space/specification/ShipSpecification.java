package com.space.specification;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShipSpecification implements Specification<Ship> {
    public static final String NAME = "name";
    public static final String PLANET = "planet";
    public static final String SHIP_TYPE = "shipType";
    public static final String AFTER = "after";
    public static final String BEFORE = "before";
    public static final String MIN_RATING = "minRating";
    public static final String RATING = "rating";
    public static final String MAX_RATING = "maxRating";
    public static final String MIN_SPEED = "minSpeed";
    public static final String SPEED = "speed";
    public static final String MAX_SPEED = "maxSpeed";
    public static final String
            MIN_CREW_SIZE = "minCrewSize";
    public static final String MAX_CREW_SIZE = "maxCrewSize";
    public static final String CREW_SIZE = "crewSize";
    public static final String IS_USED = "isUsed";
    public static final String PROD_DATE = "prodDate";

    private final Map<String, String> filter;

    public ShipSpecification(Map<String, String> filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        checkNameFilter(root, criteriaBuilder, predicates, NAME);
        checkNameFilter(root, criteriaBuilder, predicates, PLANET);
        checkShipTypeFilter(root, criteriaBuilder, predicates);
        checkMinValue(root, criteriaBuilder, predicates, MIN_SPEED, SPEED);
        checkMaxValue(root, criteriaBuilder, predicates, MAX_SPEED, SPEED);
        checkMinValue(root, criteriaBuilder, predicates, MIN_RATING, RATING);
        checkMaxValue(root, criteriaBuilder, predicates, MAX_RATING, RATING);
        checkMinValue(root, criteriaBuilder, predicates, MIN_CREW_SIZE, CREW_SIZE);
        checkMaxValue(root, criteriaBuilder, predicates, MAX_CREW_SIZE, CREW_SIZE);
        checkIsUsedFilter(root, criteriaBuilder, predicates);
        checkMaxDate(root, criteriaBuilder, predicates, BEFORE, PROD_DATE);
        checkMinDate(root, criteriaBuilder, predicates, AFTER, PROD_DATE);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void checkIsUsedFilter(Root<Ship> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (filter.get(IS_USED) != null)
            predicates.add(criteriaBuilder.equal(root.get(IS_USED),
                    Boolean.parseBoolean(filter.get(IS_USED))));
    }

    private void checkMaxValue(Root<Ship> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String maxSpeed, String speed) {
        if (filter.get(maxSpeed) != null)
            predicates.add(criteriaBuilder.le(root.get(speed),
                    Double.parseDouble(filter.get(maxSpeed))));
    }

    private void checkMinValue(Root<Ship> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String minSpeed, String speed) {
        if (filter.get(minSpeed) != null)
            predicates.add(criteriaBuilder.ge(root.get(speed),
                    Double.parseDouble(filter.get(minSpeed))));
    }

    private void checkShipTypeFilter(Root<Ship> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (filter.get(SHIP_TYPE) != null)
            predicates.add(criteriaBuilder.equal(root.get(SHIP_TYPE)
                    , ShipType.valueOf(filter.get(SHIP_TYPE))));
    }

    private void checkNameFilter(Root<Ship> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String name) {
        if (filter.get(name) != null)
            predicates.add(criteriaBuilder.like(root.get(name)
                    , "%" + filter.get(name) + "%"));
    }

    private void checkMaxDate(Root<Ship> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates,
                              String before, String date) {
        if (filter.get(before) != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(date),
                    new Date(Long.parseLong(filter.get(before)))));
        }
    }

    private void checkMinDate(Root<Ship> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates,
                              String after, String date) {
        if (filter.get(after) != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(date),
                    new Date(Long.parseLong(filter.get(after)))));
        }
    }
}