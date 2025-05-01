package com.spring.jwt.SparePart;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "spare_part")
public class SparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sparePart_seq")
    @SequenceGenerator(name = "sparePart_seq", sequenceName = "sparePart_seq", allocationSize = 1, initialValue = 21000)
    @Column(name = "sparePartId")
    private Integer sparePartId;

    @Column(name = "part_name", nullable = false)
    private String partName;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "manufacturer name cannot be blank")
    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @Column(name = "manufacturer", nullable = false)
    @NotBlank(message = "manufacturer name cannot be blank")
    private String manufacturer;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "update_At", nullable = false)
    private LocalDate updateAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "spare_part_photos",
            joinColumns = @JoinColumn(name = "spare_part_id", referencedColumnName = "sparePartId", nullable = false)
    )
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    @BatchSize(size = 10)
    private List<byte[]> photo = new ArrayList<>();

    @Column(name = "part_number", nullable = false)
    private String partNumber;

    @Column
    private Integer cGST;

    @Column
    private Integer sGST;

    @Column
    private Integer totalGST;

    @Column
    private Integer buyingPrice;

    @Column
    private String vendor;

}
