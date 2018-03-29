/**
 * 
 */
/**
 * @author User
 *
 */
@org.hibernate.annotations.GenericGenerator(
name = "ID_GENERATOR",
strategy = "enhanced-sequence",
parameters = {
  @org.hibernate.annotations.Parameter(
  name = "sequence_name",
  value = "NAAKCIIBY_ENCHANCED_SEQUENCE"
  ),
  @org.hibernate.annotations.Parameter(
  name = "initial_value",
  value = "1000"
  )
})
@org.hibernate.annotations.GenericGenerator(
		name = "productInfoKeyGenerator", 
		strategy = "foreign", 
		parameters = 
		@org.hibernate.annotations.Parameter(name = "property", value = "product")
)
package by.naakcii.repository.model;