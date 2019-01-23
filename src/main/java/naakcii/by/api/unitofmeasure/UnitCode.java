package naakcii.by.api.unitofmeasure;

import java.math.BigDecimal;
import java.util.Optional;

public enum UnitCode {
	
	KG("кг", new BigDecimal("0.100")), 
	PC("шт.", new BigDecimal("1"));
	
	private String representation;
	private BigDecimal defaultStep;
	
	UnitCode(String representation, BigDecimal defaultStep) {
		this.representation = representation;
		this.defaultStep = defaultStep;
	}
	
	public String getRepresentation() {
		return this.representation;
	}
	
	public BigDecimal getDefaultStep() {
		return this.defaultStep;
	}
	
	public static Optional<UnitCode> getByRepresentation(String representation) {		
		for (UnitCode unitCode : UnitCode.values()) {
			if (unitCode.representation.equalsIgnoreCase(representation)) {
				return Optional.of(unitCode);
			}	
		}
		
		return Optional.empty();
	}
}
