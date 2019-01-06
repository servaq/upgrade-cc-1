package io.campsite.api.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Reservation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-05T03:01:39.117Z")

public class ReservationDto {

	private static final long serialVersionUID = 5029921423594336334L;

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("sinceDate")
	private String sinceDate = null;

	@JsonProperty("untilDate")
	private String untilDate = null;

	@JsonProperty("fullname")
	private String fullname = null;

	@JsonProperty("email")
	private String email = null;

	public ReservationDto id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ReservationDto sinceDate(String sinceDate) {
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

	public ReservationDto untilDate(String untilDate) {
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

	public ReservationDto fullname(String fullname) {
		this.fullname = fullname;
		return this;
	}

	/**
	 * Get fullname
	 * 
	 * @return fullname
	 **/
	@ApiModelProperty(value = "")

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public ReservationDto email(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Get email
	 * 
	 * @return email
	 **/
	@ApiModelProperty(value = "")

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ReservationDto reservation = (ReservationDto) o;
		return Objects.equals(this.id, reservation.id) && Objects.equals(this.sinceDate, reservation.sinceDate)
				&& Objects.equals(this.untilDate, reservation.untilDate)
				&& Objects.equals(this.fullname, reservation.fullname) && Objects.equals(this.email, reservation.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, sinceDate, untilDate, fullname, email);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Reservation {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    sinceDate: ").append(toIndentedString(sinceDate)).append("\n");
		sb.append("    untilDate: ").append(toIndentedString(untilDate)).append("\n");
		sb.append("    fullname: ").append(toIndentedString(fullname)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
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
