package dhbw.on.webdev.controller._training_session;

import dhbw.on.webdev.pdf.PDFHelper;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/pdf")
public class PdfResource {

    @Inject
    PDFHelper pdfHelper;

    @GET
    @Path("/download")
    @Produces("application/pdf")
    public Response downloadPdf() {
        byte[] pdf = pdfHelper.createPdf();
        return Response.ok(pdf)
                .header("Content-Disposition", "attachment; filename=\"mydocument.pdf\"")
                .build();
    }
}
