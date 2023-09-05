package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Plant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlantDto {

    private Long id;
    private String contentNum;
    private String plantName;

    private String plantInfo;

    private String height;

    private String breed;
    private String manage;

    private String growthRate;

    private String growthTmp;
    private String fertilizer;
    private String plantSpring;

    private String plantSummner;
    private String plantAutumn;

    private String plantWinter;
    private String category;

    private String season;
    private String growth;
    private String image;
    private String thumbFile;

    private String humidity;

    private String light;

    private String special;
    public static PlantDto convertPlantToDto(Plant plant) {
        return new PlantDto(plant.getId(), plant.getContentNum(), plant.getPlantName(),plant.getPlantInfo(),plant.getHeight(),
                plant.getBreed(),  plant.getManage(),plant.getGrowthRate(),plant.getGrowthTmp(),
                plant.getFertilizer(), plant.getPlantSpring(), plant.getPlantSummner(), plant.getPlantAutumn()
                ,plant.getPlantWinter(), plant.getCategory(), plant.getSeason(), plant.getGrowth(), plant.getImage(), plant.getThumbFile(),
                plant.getHumidity(), plant.getLight(), plant.getSpecial()
        );

    }
}
