package model;

import javafx.util.Pair;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Database {
    public static int recordsNum = 1000000;
    public static Stack<String> codeStack = new Stack<>();
    public static AVLTree<Person> fullNameAVLTree = new AVLTree<>();
    public static AVLTree<Person> nameAVLTree = new AVLTree<>();
    public static AVLTree<Person> lastNameAVLTree = new AVLTree<>();
    public static AVLTree<Person> codeAVLTree = new AVLTree<>();

    public static void generateRecords(){
        try {
            ArrayList<Pair<String, String>> genderAndNames = new ArrayList<>(); // Males and females will be 50/50 in each age group
            ArrayList<String> lastNames = new ArrayList<>(); // Proportion is irrelevant
            ArrayList<Pair<Integer, String>> iterationsPerNation = new ArrayList<>(); // Depends on recordsNum
            ArrayList<Pair<Double, Pair<Integer, Integer>>> percentagePerAgeGroup = new ArrayList<>(); // Distribution will be the same for all nations
            Random random = new Random();

            // Fill in the percentage per age group based on the referenced page "index mundi"
            percentagePerAgeGroup.add(new Pair<>(0.1872, new Pair<>(0, 14)));
            percentagePerAgeGroup.add(new Pair<>(0.1312, new Pair<>(15, 24)));
            percentagePerAgeGroup.add(new Pair<>(0.3929, new Pair<>(25, 54)));
            percentagePerAgeGroup.add(new Pair<>(0.1294, new Pair<>(55, 64)));
            percentagePerAgeGroup.add(new Pair<>(0.1603, new Pair<>(65, 95)));

            // Generate the random unrepeatable codes
            permutation("", "ABCDE12345");

            // Get the files from the data folder. The information is from the specified sites in the documentation
            File namesFile = new File("data/babynames.csv");
            File lastNamesFile = new File("data/Names_2010Census.csv");
            File populationFile = new File("data/american_population_2020.csv");

            BufferedReader namesBr = new BufferedReader(new InputStreamReader(new FileInputStream(namesFile)));
            BufferedReader lastNamesBr = new BufferedReader(new InputStreamReader(new FileInputStream(lastNamesFile)));
            BufferedReader populationBr = new BufferedReader(new InputStreamReader(new FileInputStream(populationFile)));

            String line = "";

            while ((line = namesBr.readLine()) != null) {
                String[] nameAndGenderArr = line.split(",");

                if (nameAndGenderArr[1].equals("boy")){
                    genderAndNames.add(new Pair<>("Male", nameAndGenderArr[0]));
                } else {
                    genderAndNames.add(new Pair<>("Female", nameAndGenderArr[0]));
                }
            }

            line = lastNamesBr.readLine();

            while ((line = lastNamesBr.readLine()) != null) {
                lastNames.add(line);
            }

            line = populationBr.readLine();

            while ((line = populationBr.readLine()) != null) {
                String[] nationAndPercentageArr = line.split(",");
                double percentageRep = Double.parseDouble(nationAndPercentageArr[3]);

                iterationsPerNation.add(new Pair<Integer, String>((int) (recordsNum*percentageRep), nationAndPercentageArr[0]));
            }

            // Once the load of all the data is done, start simulating the generation
            int nationCounter = 0;
            int actualNationIterations = 0;

            // Firstly, iterate depending on nation
            for (int i = 0; i < recordsNum; i++) {
                Pair<Integer, String> actualNation = iterationsPerNation.get(nationCounter);

                if (actualNation.getKey() > actualNationIterations) {
                    actualNationIterations++;

                    int ageGroup1Iterations = (int) (percentagePerAgeGroup.get(0).getKey() * actualNation.getKey());
                    int ageGroup2Iterations = (int) (percentagePerAgeGroup.get(1).getKey() * actualNation.getKey());
                    int ageGroup3Iterations = (int) (percentagePerAgeGroup.get(2).getKey() * actualNation.getKey());
                    int ageGroup4Iterations = (int) (percentagePerAgeGroup.get(3).getKey() * actualNation.getKey());
                    int ageGroup5Iterations = (int) (percentagePerAgeGroup.get(4).getKey() * actualNation.getKey());

                    // Secondly, iterate depending on age groups
                    for (int j = 0; j < ageGroup1Iterations; j++) {
                        // Generation of all required data for a record

                        Pair<String, String> genderAndNameSelection;

                        int minAge = percentagePerAgeGroup.get(0).getValue().getKey();
                        int maxAge = percentagePerAgeGroup.get(0).getValue().getValue();
                        int age = random.nextInt((maxAge + 1) - minAge) + minAge;
                        LocalDate dateOfBirth = LocalDate.now().minusDays(random.nextInt(200)).minusYears(age);

                        String code = codeStack.pop();
                        String nationality = actualNation.getValue();
                        double height = (random.nextInt(50) + 150) * 0.01;

                        // Represent gender proportionally
                        // Boys
                        if (j < ageGroup1Iterations/2) {
                            genderAndNameSelection = genderAndNames.get(random.nextInt(3437));
                        } else { // Girls
                            genderAndNameSelection = genderAndNames.get(random.nextInt(3344) + 3437);
                        }

                        String name = genderAndNameSelection.getValue();
                        String lastName = lastNames.get(random.nextInt(162254));
                        String fullName = name + " " + lastName;
                        String gender = genderAndNameSelection.getKey();

                        // TODO implement comparator methods to add to the correspondent trees

                    }

                    for (int j = 0; j < ageGroup2Iterations; j++) {
                        // Generation of all required data for a record

                        Pair<String, String> genderAndNameSelection;

                        int minAge = percentagePerAgeGroup.get(1).getValue().getKey();
                        int maxAge = percentagePerAgeGroup.get(1).getValue().getValue();
                        int age = random.nextInt((maxAge + 1) - minAge) + minAge;
                        LocalDate dateOfBirth = LocalDate.now().minusDays(random.nextInt(200)).minusYears(age);

                        String code = codeStack.pop();
                        String nationality = actualNation.getValue();
                        double height = (random.nextInt(50) + 150) * 0.01;

                        // Represent gender proportionally
                        // Boys
                        if (j < ageGroup1Iterations/2) {
                            genderAndNameSelection = genderAndNames.get(random.nextInt(3437));
                        } else { // Girls
                            genderAndNameSelection = genderAndNames.get(random.nextInt(3344) + 3437);
                        }

                        String name = genderAndNameSelection.getValue();
                        String lastName = lastNames.get(random.nextInt(162254));
                        String fullName = name + " " + lastName;
                        String gender = genderAndNameSelection.getKey();

                        // TODO implement comparator methods to add to the correspondent trees
                    }

                    for (int j = 0; j < ageGroup3Iterations; j++) {
                        // Generation of all required data for a record

                        Pair<String, String> genderAndNameSelection;

                        int minAge = percentagePerAgeGroup.get(2).getValue().getKey();
                        int maxAge = percentagePerAgeGroup.get(2).getValue().getValue();
                        int age = random.nextInt((maxAge + 1) - minAge) + minAge;
                        LocalDate dateOfBirth = LocalDate.now().minusDays(random.nextInt(200)).minusYears(age);

                        String code = codeStack.pop();
                        String nationality = actualNation.getValue();
                        double height = (random.nextInt(50) + 150) * 0.01;

                        // Represent gender proportionally
                        // Boys
                        if (j < ageGroup1Iterations/2) {
                            genderAndNameSelection = genderAndNames.get(random.nextInt(3437));
                        } else { // Girls
                            genderAndNameSelection = genderAndNames.get(random.nextInt(3344) + 3437);
                        }

                        String name = genderAndNameSelection.getValue();
                        String lastName = lastNames.get(random.nextInt(162254));
                        String fullName = name + " " + lastName;
                        String gender = genderAndNameSelection.getKey();

                        // TODO implement comparator methods to add to the correspondent trees
                    }

                    for (int j = 0; j < ageGroup4Iterations; j++) {

                        // Represent gender proportionally
                        if (j < ageGroup4Iterations/2) {

                        } else {

                        }
                    }

                    for (int j = 0; j < ageGroup5Iterations; j++) {

                        // Represent gender proportionally
                        if (j < ageGroup5Iterations/2) {

                        } else {

                        }
                    }

                } else {
                    nationCounter++;
                    actualNationIterations = 0;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void permutation(String prefix, String str) {
        int n = str.length();
        if (n == 0) {
            codeStack.add(prefix);
        }
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
        }
    }
}
