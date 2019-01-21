package naakcii.by.api.product;

import java.util.Optional;

public enum Unit {
	
	KG("кг"), 
	PC("шт");
	
	private String representation;
	
	Unit(String representation) {
		this.representation = representation;
	}
	
	public String getRepresentation() {
		return this.representation;
	}
	
	public static Optional<Unit> getByRepresentation(String representation) {		
		for (Unit unit : Unit.values()) {
			if (unit.representation.equalsIgnoreCase(representation)) {
				return Optional.of(unit);
			}	
		}
		
		return Optional.empty();
	}
}
