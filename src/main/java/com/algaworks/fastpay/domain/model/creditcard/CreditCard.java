package com.algaworks.fastpay.domain.model.creditcard;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Locale;

@Data
@EqualsAndHashCode
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {

	@Id
	@Builder.Default
	private String id = "tok_" + RandomStringUtils.secure().nextAlphanumeric(32).toLowerCase(Locale.ROOT);

	private String number;
	private String cvv;

	private String holderName;
	private String holderDocument;

	private Integer expMonth;
	private Integer expYear;

	private String brand;

	private String customerCode;

	private OffsetDateTime assignmentExpiresAt; //Assignment expiration, not card expiration

	public boolean isCardExpired() {
		if (expMonth == null || expYear == null) {
			return false;
		}

		OffsetDateTime expirationDate = YearMonth.of(expYear, expMonth)
				.plusMonths(1)
				.atDay(1)
				.atStartOfDay()
				.atOffset(OffsetDateTime.now().getOffset());

		return OffsetDateTime.now().isEqual(expirationDate) || OffsetDateTime.now().isAfter(expirationDate);
	}

	public boolean isAssignmentExpired() {
		if (assignmentExpiresAt == null) {
			return false;
		}

		return assignmentExpiresAt.isBefore(OffsetDateTime.now());
	}

	public void removeExpiration() {
		this.setAssignmentExpiresAt(null);
	}

	public boolean isAssigned() {
		return getCustomerCode() != null;
	}
}
