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
            if (slide.getPageElements() != null) {
                System.out.printf("- Slide #%s contains %s elements.\n", i + 1, slide.getPageElements().size());
            }
        }


        List<Request> requests = new ArrayList<>();

        //region <New Slide>
        // create new slide request
        CreateSlideRequest csr = new CreateSlideRequest();


        csr.setInsertionIndex(0);

        Request createRequest = new Request();
        createRequest.setCreateSlide(csr);

        requests.add(createRequest);
        //endregion


        //region <Populate Slide>


        InsertTextRequest itextrequest = new InsertTextRequest();
        itextrequest.setObjectId(presentation.getSlides().get(0).getPageElements().get(0).getObjectId());
        itextrequest.setText("Hello?");
        Request updateRequest = new Request();
        updateRequest.setInsertText(itextrequest);
        requests.add(updateRequest);

        //endregion

        // region <Execute requests>
        BatchUpdatePresentationRequest bupRequest = new BatchUpdatePresentationRequest();
        bupRequest.setWriteControl(new WriteControl().setRequiredRevisionId(presentation.getRevisionId()));
        bupRequest.setRequests(requests);
        BatchUpdatePresentationResponse response = service.presentations().batchUpdate(presentationId, bupRequest).execute();
        response.getReplies().forEach(reply -> System.out.println(reply.toString()));
        //endregion
    }
}