package server;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;

import common.AddressReport;
import java.util.logging.Logger;

/**
 * Manages writing data to Excel files.
 */
public class ExcelFileManager {

    private static final String[] HEADER_TITLES = {"Address", "Report Count", "Link"};
    private static final Logger LOGGER = Logger.getLogger(ExcelFileManager.class.getName());

    /**
     * Saves a list of `ReportEntry` objects to an Excel file.
     *
     * @param directoryPath The directory where the Excel file will be saved.
     * @param entries       The list of report entries to be written.
     * @return True if the operation is successful, false otherwise.
     */
    public boolean saveToExcel(String directoryPath, ArrayList<AddressReport> entries) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            LOGGER.severe("Directory path is invalid.");
            return false;
        }

        if (entries == null || entries.isEmpty()) {
            LOGGER.severe("No entries to write.");
            return false;
        }

        try {
            String timestamp = new SimpleDateFormat("dd-MM-yy_HH-mm-ss").format(Calendar.getInstance().getTime());
            String filePath = directoryPath + "/Report_Summary_" + timestamp + ".xls";

            HSSFWorkbook workbook = createWorkbook(entries);

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }

            workbook.close();
            LOGGER.info("Excel file created successfully: " + filePath);
            return true;
        } catch (Exception e) {
            LOGGER.severe("Failed to create Excel file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates an HSSFWorkbook with the provided entries.
     *
     * @param entries The list of report entries.
     * @return A populated HSSFWorkbook instance.
     */
    private HSSFWorkbook createWorkbook(ArrayList<AddressReport> entries) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report_Summary");

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADER_TITLES.length; i++) {
            headerRow.createCell(i).setCellValue(HEADER_TITLES[i]);
        }

        int currentRow = 1;
        for (AddressReport entry : entries) {
            HSSFRow row = sheet.createRow(currentRow);
            row.createCell(0).setCellValue(entry.getAddress());
            row.createCell(1).setCellValue(Integer.parseInt(entry.getReportCount()));
            row.createCell(2).setCellValue(entry.getLink().getText());
            currentRow++;
        }

        for (int colIndex = 0; colIndex < HEADER_TITLES.length; colIndex++) {
            sheet.autoSizeColumn(colIndex);
        }

        return workbook;
    }
}
