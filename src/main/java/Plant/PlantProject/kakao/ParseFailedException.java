package Plant.PlantProject.kakao;

public class ParseFailedException extends RuntimeException{

    private static final String MESSAGE = "데이터 파싱에 실패하였습니다.";

    public ParseFailedException(){
        super(MESSAGE);
    }
}