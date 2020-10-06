package com.softuni.dtos.dtos;

import com.softuni.dtos.entities.Order;
import org.springframework.stereotype.Component;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Component
public class GameDto {
    private String title;
    private BigDecimal price;
    private double size;
    private String trailer;
    private String imageThumbnail;
    private String description;
    private LocalDate releaseDate;
//    private Set<OrderDto> orders;

    public GameDto() {
    }

    public GameDto(String title, BigDecimal price, double size, String trailer, String imageThumbnail, String description, LocalDate releaseDate) {
        this.title = title;
        this.price = price;
        this.size = size;
        this.trailer = trailer;
        this.imageThumbnail = imageThumbnail;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    @Pattern(regexp = "^[A-Z].{3,100}")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Pattern(regexp = "(.{11})", message = "Trailer is invalid.")
    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    @Pattern(regexp = "^(http:\\/\\/|https:\\/\\/).+", message = "Thumbnail is invalid")
    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    @DecimalMin(value="1", inclusive = true, message = "Size is invalid")
    public double getSize() {
        return size;
    }
    public void setSize(double size) { this.size = size; }

    @DecimalMin(value="1", inclusive = true, message = "Price is invalid")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Pattern(regexp="^.{20,}", message = "Description length is invalid")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

//    public Set<OrderDto> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(Set<OrderDto> orders) {
//        this.orders = orders;
//    }
}
