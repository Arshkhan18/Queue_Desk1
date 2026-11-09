package com.arshkhan.queuedesk.simulator;

import java.util.Scanner;

public class QueueSimulator {

    public static void run() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter starting ticket number: ");
        int start = sc.nextInt();

        System.out.print("Enter number of customers: ");
        int customers = sc.nextInt();

        int served = 0;
        int vip = 0;
        int priorityRecheck = 0;

        for (int i = 0; i < customers; i++) {

            int ticketNumber = start + i;

            System.out.println("Now serving ticket #" + ticketNumber);

            served++;

            if (ticketNumber % 5 == 0) {
                System.out.println("VIP lane ticket!");
                vip++;
            }

            if (ticketNumber % 10 == 0) {
                System.out.println("Priority recheck!");
                priorityRecheck++;
            }
        }

        System.out.println("\n----- Queue Summary -----");

        System.out.println("Total tickets served: " + served);
        System.out.println("VIP tickets: " + vip);
        System.out.println("Priority rechecks: " + priorityRecheck);

        sc.close();
    }
}