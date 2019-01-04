package io.campsite.api.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * DateRange
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-04T15:28:04.296Z")

public class DateRangeDto {

	@JsonProperty("sinceDate")
	private String sinceDate = null;

	@JsonProperty("untilDate")
	private String untilDate = null;

	public DateRangeDto sinceDate(String sinceDate) {
		this.sinceDate = sinceDate;
		return this;
	}

	/**
	 * Get sinceDate
	 * 
	 * @return sinceDate
	 **/
	@ApiModelProperty(value = "")

	public String getSinceDate() {
		return sinceDate;
	}

	public void setSinceDate(String sinceDate) {
		this.sinceDate = sinceDate;
	}

	public DateRangeDto untilDate(String untilDate) {
		this.untilDate = untilDate;
		return this;
	}

	/**
	 * Get untilDate
	 * 
	 * @return untilDate
	 **/
	@ApiModelProperty(value = "")

	public String getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(String untilDate) {
		this.untilDate = untilDate;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DateRangeDto dateRange = (DateRangeDto) o;
		return Objects.equals(this.sinceDate, dateRange.sinceDate)
				&& Objects.equals(this.untilDate, dateRange.untilDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sinceDate, untilDate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DateRange {\n");

		sb.append("    sinceDate: ").append(toIndentedString(sinceDate)).append("\n");
		sb.append("    untilDate: ").append(toIndentedString(untilDate)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
