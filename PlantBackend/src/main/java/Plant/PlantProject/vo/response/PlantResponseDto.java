package Plant.PlantProject.vo.response;

import Plant.PlantProject.domain.Entity.Plant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlantResponseDto {

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
    public static PlantResponseDto convertPlantToDto(Plant plant) {
        return new PlantResponseDto(plant.getId(), plant.getContentNum(), plant.getPlantName(),plant.getPlantInfo(),plant.getHeight(),
                plant.getBreed(),  plant.getManage(),plant.getGrowthRate(),plant.getGrowthTmp(),
                plant.getFertilizer(), plant.getPlantSpring(), plant.getPlantSummner(), plant.getPlantAutumn()
                ,plant.getPlantWinter(), plant.getCategory(), plant.getSeason(), plant.getGrowth(), plant.getImage(), plant.getThumbFile(),
                plant.getHumidity(), plant.getLight(), plant.getSpecial()
        );

    }
}
