package fr.greta.golf.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.entities.Game;
import fr.greta.golf.entities.TimePerHPerG;
import fr.greta.golf.model.Player;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GameRatePdfImpl implements IGameRateDocument {

    @Override
    public void generateGameRate(Competition competition, HttpServletResponse response) {
        List<Game> games = competition.getGames();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.setPageSize(PageSize.A4.rotate());
            document.open();

            String[] tab = competition.getDateCompetition().split("-");
            String date = tab[2] + tab[1] + tab[0];
            String str = competition.getName() + " - " + competition.getCourse().getName() + " - " + date;
            document.add(new Phrase(str));
            int nbHoles = competition.getCourse().getHoles().size();
            PdfPTable table = new PdfPTable(nbHoles + 3);
            float[] width = new float[nbHoles + 3];
            width[0] = 0.5f; width[1] = 3f; width[2] = 1f;
            for (int w = 0; w < nbHoles; w++)
                width[w+3] = 1f;
            PdfPCell cell;

            for (int i = 0; i < games.size(); i++){
                table.completeRow();
                if (i == 0 || i == games.size()/2){
                    if (i == games.size()/2){
                        document.add(table);
                        document.add(new Phrase("Page 1", FontFactory.getFont("Arial", 7)));
                        document.setPageSize(PageSize.A4.rotate());
                        document.newPage();
                    }
                    table = new PdfPTable(nbHoles + 3);
                    table.setWidths(width);
                    table.setWidthPercentage(100);
                    /*Begin of table header*/
                    cell = new PdfPCell(new Phrase("#", FontFactory.getFont("Arial", 7)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.YELLOW);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Joueurs", FontFactory.getFont("Arial", 7)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.YELLOW);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Dép.", FontFactory.getFont("Arial", 7)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.YELLOW);
                    table.addCell(cell);

                    for (int c = 0; c < nbHoles; c++){
                        cell = new PdfPCell(new Phrase(String.valueOf(c+1), FontFactory.getFont("Arial", 7)));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(BaseColor.YELLOW);
                        table.addCell(cell);
                    }
                    /*End of table header*/
                }

                cell = new PdfPCell(new Phrase(String.valueOf(games.get(i).getNum()), FontFactory.getFont("Arial", 7)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                Paragraph p = new Paragraph();
                p.setFont(FontFactory.getFont("Arial", 7));
                for (Player player: games.get(i).getPlayers()){
                    p.add(new Phrase(player.getName() + "\n"));
                }
                cell = new PdfPCell(p);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                for (TimePerHPerG perHPerG: games.get(i).getTimesPerHPerG()){
                    cell = new PdfPCell(new Phrase(perHPerG.getTime(), FontFactory.getFont("Arial", 7)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            document.add(table);
            document.add(new Phrase("Page 2", FontFactory.getFont("Arial", 7)));
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
