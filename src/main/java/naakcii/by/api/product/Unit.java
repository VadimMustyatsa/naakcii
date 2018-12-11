package naakcii.by.api.product;

public enum Unit {
	
	KG("кг"), 
	PC("шт.");
	
	private String representation;
	
	Unit(String representation) {
		this.representation = representation;
	}
	
	public String getRepresentation() {
		return this.representation;
	}
}
