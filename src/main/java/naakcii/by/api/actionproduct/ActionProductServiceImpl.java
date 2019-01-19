package naakcii.by.api.actionproduct;

import naakcii.by.api.action.Action;
import naakcii.by.api.action.ActionRepository;
import naakcii.by.api.util.ObjectFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActionProductServiceImpl implements ActionProductService {
	
	private ActionRepository actionRepository;
	private ObjectFactory objectFactory;
	
	@Autowired
	public ActionProductServiceImpl(ActionRepository actionRepository, ObjectFactory objectFactory) {
		this.actionRepository = actionRepository;
		this.objectFactory = objectFactory;
	}
	
	private Calendar getCurrentDate() {
		Calendar currentDate = Calendar.getInstance();
		return new GregorianCalendar(currentDate.get(Calendar.YEAR), 
									 currentDate.get(Calendar.MONTH), 
									 currentDate.get(Calendar.DAY_OF_MONTH)
		);
	}

	@Override
	public List<ActionProductDTO> getAllProductsByChainIdsAndSubcategoryIds(Set<Long> subcategoryIds, Set<Long> chainIds, Pageable pageable) {
		return actionRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoryIds, chainIds, getCurrentDate(), getCurrentDate(), pageable)
				.stream()
				.filter(Objects::nonNull)
				.map((Action action) -> objectFactory.getInstance(ActionProductDTO.class, action))
				.collect(Collectors.toList());
	}
}