package com.codecool.ehotel.service.buffet;

import com.codecool.ehotel.model.Buffet;
import com.codecool.ehotel.model.MealDurability;
import com.codecool.ehotel.model.MealPortion;
import com.codecool.ehotel.model.MealType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BuffetServiceImpl implements BuffetService {


    @Override
    public void refillBuffet(Buffet buffet, Collection<RefillRequest> refillRequests) {
        List<MealPortion> mealPortions = new LinkedList<>();
        LocalDateTime refillTimestamp = LocalDateTime.now();

        for (RefillRequest refillRequest : refillRequests) {
            MealType mealType = refillRequest.mealType();
            int amount = refillRequest.amount();

            for (int i = 0; i < amount; i++) {
                mealPortions.add(new MealPortion(mealType, refillTimestamp));
            }
        }
        buffet.addMealPortion(mealPortions);
    }

    @Override
    public void removeMealPortion(Buffet buffet, MealPortion mealPortion) {
        List<MealPortion> mealPortions = buffet.getMealPortions();
        mealPortions.remove(mealPortion);
    }

    @Override
    public boolean consumeFreshest(Buffet buffet, MealType mealType) {
        List<MealPortion> mealPortionsByType = buffet.getMealPortionsByType(mealType);
        if (!mealPortionsByType.isEmpty()) {
            buffet.removeMealPortion(mealPortionsByType.get(mealPortionsByType.size() - 1));
            return true;
        }
        return false;
    }

    @Override
    public int collectWaste(Buffet buffet, MealDurability mealDurability, LocalDateTime time) {
        List<MealPortion> mealPortions = buffet.getMealPortions();

        int totalCost = 0;
        Iterator<MealPortion> iterator = mealPortions.iterator();
        while (iterator.hasNext()) {
            MealPortion portion = iterator.next();
            if (portion.getMealType().getDurability() == mealDurability && portion.getTimestamp().isBefore(time)) {
                totalCost += portion.getMealType().getCost();
                iterator.remove();
            }
        }
        return totalCost;
    }
}