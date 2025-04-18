/*
 * Copyright (c) 2003-2004 Ben Mazur (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package megamek.common;

/**
 * Represents a volume of space set aside for carrying general cargo aboard large spacecraft and
 * mobile structures.
 */
public final class CargoBay extends Bay implements InfantryTransporter {
    private static final long serialVersionUID = 4161027191694822726L;

    /**
     * The default constructor is only for serialization.
     */
    private CargoBay() {
        totalSpace = 0;
        currentSpace = 0;
    }

    /**
     * Create a space for the given tonnage of troops. For this class, only the
     * weight of the troops (and their equipment) are considered; if you'd like
     * to think that they are stacked like lumber, be my guest.
     *
     * @param space The weight of troops (in tons) this space can carry.
     */
    public CargoBay(double space, int doors, int bayNumber) {
        totalSpace = space;
        currentSpace = space;
        this.doors = doors;
        this.bayNumber = bayNumber;
        currentdoors = doors;
    }

    @Override
    public boolean canLoad(Entity unit) {
        if (!unit.isInfantry()) {
            return false;
        }
        // Infantry is only restricted by adjacency requirements (TW pp. 223 - 225)
        return true;
    }

    @Override
    public boolean canUnloadUnits() {
        // Infantry is only restricted by adjacency requirements (TW pp. 223 - 225)
        return super.canUnloadUnits() || troops.stream()
            .map(unit -> game.getEntity(unit))
            .anyMatch(e -> (e != null && e.isInfantry()));
    }

    // Use the calculated original weight for infantry in cargo bays
    @Override
    public double spaceForUnit(Entity unit) {
        return Math.round(unit.getWeight() / unit.getInternalRemainingPercent());
    }

    @Override
    public String getUnusedString(boolean showRecovery) {
        StringBuffer returnString = new StringBuffer("Cargo Space "
                + numDoorsString() + " - ");

        if (getUnused() != Math.round(getUnused())) {
            returnString.append(String.format("%1$,.3f", getUnused()));
        } else {
            returnString.append(String.format("%1$,.0f", getUnused()));
        }

        returnString.append(" tons");
        return returnString.toString();
    }

    @Override
    public String getType() {
        return "Cargo";
    }

    @Override
    public String toString() {
        String bayType = "cargobay";
        return this.bayString(
                bayType,
                totalSpace,
                doors,
                bayNumber
        );
    }

    @Override
    public boolean isCargo() {
        return true;
    }
}
