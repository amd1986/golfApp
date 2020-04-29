package fr.greta.golf.services;

import fr.greta.golf.model.Player;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ExtractFromXlsxImpl implements IExtractor {
    @Override
    public List<Player> extractPlayers(MultipartFile file) {
        List<Player> players = new ArrayList<>();
        try (InputStream fis = file.getInputStream()) {
            try (Workbook wb = new XSSFWorkbook(fis)) {

                for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                    Sheet sheet = wb.getSheetAt(i);
                    int idxName = 0;
                    int idxLicence = 0;
                    int idxIndex = 0;
                    int idxSerie = 0;
                    int idxRep = 0;
                    int idxNat = 0;
                    int idxClub = 0;
                    int idxComment = 0;
                    for (Row row : sheet) {
                        for (Cell cell : row) {
                            if (cell.getCellType() == CellType.STRING) {
                                if (idxName == 0 && cell.getStringCellValue().trim().equals("Nom et Prénom")) {
                                    idxName = cell.getColumnIndex();
                                }
                                if (idxLicence == 0 && cell.getStringCellValue().trim().equals("Licence")) {
                                    idxLicence = cell.getColumnIndex();
                                }
                                if (idxIndex == 0 && cell.getStringCellValue().trim().equals("Idx")) {
                                    idxIndex = cell.getColumnIndex();
                                }
                                if (idxSerie == 0 && cell.getStringCellValue().trim().equals("Série")) {
                                    idxSerie = cell.getColumnIndex();
                                }
                                if (idxRep == 0 && cell.getStringCellValue().trim().equals("Rep.")) {
                                    idxRep = cell.getColumnIndex();
                                }
                                if (idxNat == 0 && cell.getStringCellValue().trim().equals("Nat.")) {
                                    idxNat = cell.getColumnIndex();
                                }
                                if (idxClub == 0 && cell.getStringCellValue().trim().equals("Club de licence")) {
                                    idxClub = cell.getColumnIndex();
                                }
                                if (idxComment == 0 && cell.getStringCellValue().trim().equals("Commentaire")) {
                                    idxComment = cell.getColumnIndex();
                                }
                            }
                        }
                        if (idxName != 0 && idxLicence != 0 && idxIndex != 0 && idxSerie != 0 && idxRep != 0 && idxNat != 0 && idxClub != 0 && idxComment != 0)
                            break;
                    }
                    Player player = new Player();
                    for (Row row : sheet) {
                        for (Cell cell : row) {

                            if (cell.getCellType() != CellType._NONE) {
                                if (cell.getColumnIndex() == idxName) player.setName(cell.getStringCellValue());
                                if (cell.getColumnIndex() == idxLicence) {
                                    try {
                                        double licence;
                                        if (cell.getCellType() == CellType.STRING) {
                                            licence = Double.parseDouble(cell.getStringCellValue().trim());
                                        } else {
                                            licence = cell.getNumericCellValue();
                                        }
                                        int num = (int) licence;
                                        player.setLicence(num);
                                    } catch (NumberFormatException ignored) {
                                    }

                                }
                                if (cell.getColumnIndex() == idxIndex) {
                                    try {
                                        String str = cell.getStringCellValue().trim().replace(",", ".");
                                        player.setIdx(Double.parseDouble(str));
                                    } catch (NumberFormatException ignored) {
                                    }

                                }
                                if (cell.getColumnIndex() == idxSerie) player.setSerie(cell.getStringCellValue());
                                if (cell.getColumnIndex() == idxRep) player.setRep(cell.getStringCellValue());
                                if (cell.getColumnIndex() == idxNat) player.setNat(cell.getStringCellValue());
                                if (cell.getColumnIndex() == idxClub) player.setClub(cell.getStringCellValue());
                                if (cell.getColumnIndex() == idxComment) player.setComment(cell.getStringCellValue());

                            }

                        }
                        if (player.getName() == null || player.getLicence() == 0 || player.getIdx() == 0 || player.getSerie() == null ||
                                player.getRep() == null || player.getNat() == null || player.getClub() == null ) {
                            continue;
                        } else {
                            players.add(player);
                        }

                        player = new Player();
                        //System.out.println();
                        //System.out.println("Numéro de la ligne : " + ligne.getRowNum());

                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier introuvable !");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }
}
