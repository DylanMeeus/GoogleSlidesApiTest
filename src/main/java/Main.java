import com.google.api.services.slides.v1.Slides;
import com.google.api.services.slides.v1.model.*;
import main.service.SlideService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String APPLICATION_NAME = "Google Slides API Test";


    public static void main(String[] args) throws IOException {
        Slides service = SlideService.getSlidesService(APPLICATION_NAME);

        // Prints the number of slides and elements in a sample presentation:

        // the ID is what you find the URL bar when looking at a slide from google slides :-)
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

        List<Page> newSlides = new ArrayList<>(slides);
        Page p = new Page();
        PageElement testElement = new PageElement();
        testElement.setTitle("NIEUWE TITEL");
        p.setPageElements(new ArrayList<PageElement>(){{
            add(testElement);}});
        newSlides.add(p);
        presentation.setSlides(newSlides);
        presentation.setTitle("Test_Titlle");
        Request r = new Request();
        CreateSlideRequest csr = new CreateSlideRequest();
        String objectId = p.getObjectId();
        csr.setObjectId(objectId);
        csr.setInsertionIndex(0);
        r.setCreateSlide(csr);

        BatchUpdatePresentationRequest bupRequest = new BatchUpdatePresentationRequest();
        bupRequest.setWriteControl(new WriteControl().setRequiredRevisionId(presentation.getRevisionId()));
        bupRequest.setRequests(new ArrayList<Request>(){{
            add(r);}});
        BatchUpdatePresentationResponse response = service.presentations().batchUpdate(presentationId, bupRequest).execute();
        response.getReplies().forEach(reply -> System.out.println(reply.toString()));
    }
}