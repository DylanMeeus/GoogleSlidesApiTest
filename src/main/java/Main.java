import com.google.api.services.slides.v1.Slides;
import com.google.api.services.slides.v1.model.Page;
import com.google.api.services.slides.v1.model.Presentation;
import main.service.SlideService;

import java.io.IOException;
import java.util.List;

public class Main {
    /** Application name. */
    private static final String APPLICATION_NAME =
            "Google Slides API Test";


    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        Slides service = SlideService.getSlidesService(APPLICATION_NAME);

        // Prints the number of slides and elements in a sample presentation:
        // https://docs.google.com/presentation/d/1EAYk18WDjIG-zp_0vLm3CsfQh_i8eXc67Jo2O9C6Vuc/edit
        String presentationId = "12lcFWih53Vg6rbVq88ZJCZiFTtKeXMVYfjPiTJ6vefY";
        Presentation presentation = service.presentations().get(presentationId).execute();
        System.out.println(String.format("Presentation: %s", presentation.getTitle()));
        List<Page> slides = presentation.getSlides();
        System.out.printf("The presentation contains %s slides:\n", slides.size());
        for (int i = 0; i < slides.size(); i++) {
            Page slide = slides.get(i);
            System.out.printf("- Slide #%s contains %s elements.\n", i + 1,
                    slide.getPageElements().size());
        }
    }
}