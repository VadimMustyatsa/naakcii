package naakcii.by.api.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Action")
@NamedQueries({
	@NamedQuery(name = "Action.findAll", query = "select ac from Action ac"),
	@NamedQuery(name = "Action.findAllWithDetails", 
	query = "select ac from Action ac left join fetch ac.chain ch left join fetch ac.product p"), 
	@NamedQuery(name = "Action.findByChainId", 
		query = "select ac from Action ac where ac.id.chainId = :id"),
	@NamedQuery(name = "Action.findByChainIdWithDetails", 
	query = "select ac from Action ac left join fetch ac.chain ch left join fetch ac.product p where ac.id.chainId = :id"),
	@NamedQuery(name = "Action.findByChainIdAndProductSubcategory", 
	query = "select ac from Action ac left join fetch ac.chain ch left join fetch ac.product p where ac.id.chainId = :id1 and p.subcategory.id = :id2")
})
@org.hibernate.annotations.Immutable
public class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1525810593299011676L;


	@Embeddable
	public static class Id implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6978249943191744201L;

		@Column(name = "PRODUCT_ID")
		private Long productId;
		
		@Column(name = "CHAIN_ID")
		private Long chainId;
		
		public Id() {
			
		}
		
		public Id(Long productId, Long chainId) {
			this.productId = productId;
			this.chainId = chainId;
		}
		
		public boolean equals(Object o) {
			if(o != null && o instanceof Id ) {
				Id that = (Id) o;
				return this.productId.equals(that.productId) && 
						this.chainId.equals(that.chainId);
			}
			return false;
		}
		
		public int hashCode() {
			return productId.hashCode() + chainId.hashCode();
		}
		
	}
	
	@EmbeddedId
	private Id id = new Id();
	
	@Column(name = "PRICE")
	//@NotNull
	private double price;
	
	@Column(name = "DISCOUNT")
	private double discount;
	
	@Column(name = "DISCOUNT_PRICE")
	private double discountPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "PRODUCT_ID",
			updatable = false,
			insertable = false
	)
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "CHAIN_ID",
			updatable = false,
			insertable = false
	)
	private Chain chain;
	
	public Action() {
		
	}
	
	
	public Action(Product product, Chain chain, double price) {
		this.product = product;
		this.chain = chain;
		this.price = price;
		this.id.productId = product.getId();
		this.id.chainId = chain.getId();
		product.getActions().add(this);
		chain.getActions().add(this);
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Chain getChain() {
		return chain;
	}

	public void setChain(Chain chain) {
		this.chain = chain;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public double getDiscount() {
		return discount;
	}


	public void setDiscount(double discount) {
		this.discount = discount;
	}


	public double getDiscountPrice() {
		return discountPrice;
	}


	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}
	
}
