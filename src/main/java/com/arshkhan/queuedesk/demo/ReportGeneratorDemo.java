package com.arshkhan.queuedesk.demo;

import com.arshkhan.queuedesk.io.TicketFileLoader;
import com.arshkhan.queuedesk.model.Ticket;
import com.arshkhan.queuedesk.report.ReportGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.arshkhan.queuedesk.exception.InvalidTicketDataException;

public class ReportGeneratorDemo {

    public static void main(String[] args) {

        try {

            // Load tickets from file
            Path inputFile =
                    Path.of("src/main/resources/tickets.txt");

            List<Ticket> tickets =
                    TicketFileLoader.load(inputFile);

            // Generate report
            String report =
                    ReportGenerator.generate(tickets);

            // Display report
            System.out.println(report);

            // Save report
            Path outputFile =
                    Path.of("src/main/resources/reports/queuedesk-report.txt");

            // Create reports directory if it doesn't exist
            java.nio.file.Files.createDirectories(outputFile.getParent());

            ReportGenerator.write(report, outputFile);

            System.out.println("Report successfully generated!");

        } catch (IOException | InvalidTicketDataException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}