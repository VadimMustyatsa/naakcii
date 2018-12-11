package naakcii.by.api.country;

//Codes of countries in ISO 3166-1 Alpha-2 format.
public enum CountryCode {
	
	AZ("AZE", "Азербайджан"),
	AM("ARM", "Армения"),
	AR("ARG", "Аргентина"),
	BY("BLR", "Беларусь"),
	BG("BGR", "Болгария"),
	LV("LVA", "Латвия"),
	LT("LTU", "Литва"),
	RU("RUS", "Россия"),
	PL("POL", "Польша"),
	UA("UKR", "Украина");
	
	private String alphaCode3;
	private String countryName;
	
	CountryCode(String alphaCode3, String countryName) {
		this.alphaCode3 = alphaCode3;
		this.countryName = countryName;
	}
	
	public String getAlphaCode2() {
		return name();
	}
	
	public String getAlphaCode3() {
		return this.alphaCode3;
	}
	
	public String getCountryName() {
		return this.countryName;
	}
}
