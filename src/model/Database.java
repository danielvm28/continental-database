package model;

import com.google.gson.Gson;
import exception.DuplicateValueException;
import javafx.util.Pair;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Database {
    // Static tree structures
    public static HashSet<Integer> codeSet;
    public static AVLTree<Person> fullNameAVLTree;
    public static AVLTree<Person> nameAVLTree;
    public static AVLTree<Person> lastNameAVLTree;
    public static AVLTree<Person> codeAVLTree;

    // private attributes for serialization
    private int recordsNum;
    private ArrayList<Person> fullNameAVLLogs;
    private ArrayList<Person> nameAVLLogs;
    private ArrayList<Person> lastNameAVLLogs;
    private ArrayList<Person> codeAVLLogs;

    public Database(int recordsNum) {
        this.recordsNum = recordsNum;
        fullNameAVLLogs = new ArrayList<>();
        nameAVLLogs = new ArrayList<>();
        lastNameAVLLogs = new ArrayList<>();
        codeAVLLogs = new ArrayList<>();

        fullNameAVLTree = new AVLTree<>();
        codeSet = new HashSet<>();

        generateComparators();
    }

    public Database() {
        // Default records value
        this.recordsNum = 1000000;

        fullNameAVLLogs = new ArrayList<>();
        nameAVLLogs = new ArrayList<>();
        lastNameAVLLogs = new ArrayList<>();
        codeAVLLogs = new ArrayList<>();

        fullNameAVLTree = new AVLTree<>();
        codeSet = new HashSet<>();

        generateComparators();
    }

    public int getRecordsNum() {
        return recordsNum;
    }

    public void setRecordsNum(int recordsNum) {
        this.recordsNum = recordsNum;
    }

    public void generateComparators() {
        // Comparators to organize the AVL Trees
        Comparator<Person> nameComparator = new Comparator<Person>() {
            @Override
            public int compare(Person A, Person B) {
                int nameOutput = 0;
                int codeOutput = 0;

                String[] namesA = A.getFullName().split(" ");
                String[] namesB = B.getFullName().split(" ");

                nameOutput = namesA[0].compareTo(namesB[0]);

                if (nameOutput == 0) {
                    codeOutput = A.getCode() - B.getCode();

                    return codeOutput;
                } else {
                    return nameOutput;
                }
            }
        };

        Comparator<Person> lastNameComparator = new Comparator<Person>() {
            @Override
            public int compare(Person A, Person B) {
                int lastNameOutput = 0;
                int codeOutput = 0;

                String[] namesA = A.getFullName().split(" ");
                String[] namesB = B.getFullName().split(" ");

                lastNameOutput = namesA[1].compareTo(namesB[1]);

                if (lastNameOutput == 0) {
                    codeOutput = A.getCode() - B.getCode();

                    return codeOutput;
                } else {
                    return lastNameOutput;
                }
            }
        };

        Comparator<Person> codeComparator = new Comparator<Person>() {
            @Override
            public int compare(Person A, Person B) {
                int codeOutput = 0;

                codeOutput = A.getCode() - B.getCode();

                return codeOutput;
            }
        };

        nameAVLTree = new AVLTree<>(nameComparator);
        lastNameAVLTree = new AVLTree<>(lastNameComparator);
        codeAVLTree = new AVLTree<>(codeComparator);
    }

    public void generateRecords(){
        int recordsCounter = 0;

        try {
            ArrayList<Pair<String, String>> genderAndNames = new ArrayList<>(); // Males and females will be 50/50 in each age group
            ArrayList<String> lastNames = new ArrayList<>(); // Proportion is irrelevant
            ArrayList<Pair<Integer, String>> iterationsPerNation = new ArrayList<>(); // Depends on recordsNum
            ArrayList<Pair<Double, Pair<Integer, Integer>>> percentagePerAgeGroup = new ArrayList<>(); // Distribution will be the same for all nations

            // Fill in the percentage per age group based on the referenced page "index mundi"
            percentagePerAgeGroup.add(new Pair<>(0.1872, new Pair<>(0, 14)));
            percentagePerAgeGroup.add(new Pair<>(0.1312, new Pair<>(15, 24)));
            percentagePerAgeGroup.add(new Pair<>(0.3929, new Pair<>(25, 54)));
            percentagePerAgeGroup.add(new Pair<>(0.1294, new Pair<>(55, 64)));
            percentagePerAgeGroup.add(new Pair<>(0.1603, new Pair<>(65, 95)));

            // Get the files from the data folder. The information is from the specified sites in the documentation
            File namesFile = new File("data/babynames.csv");
            File lastNamesFile = new File("data/Names_2010Census.csv");
            File populationFile = new File("data/american_population_2020.csv");

            BufferedReader namesBr = new BufferedReader(new InputStreamReader(new FileInputStream(namesFile)));
            BufferedReader lastNamesBr = new BufferedReader(new InputStreamReader(new FileInputStream(lastNamesFile)));
            BufferedReader populationBr = new BufferedReader(new InputStreamReader(new FileInputStream(populationFile)));

            String line = "";

            // Get name and gender data
            while ((line = namesBr.readLine()) != null) {
                String[] nameAndGenderArr = line.split(",");

                if (nameAndGenderArr[1].equals("boy")){
                    genderAndNames.add(new Pair<>("Male", nameAndGenderArr[0]));
                } else {
                    genderAndNames.add(new Pair<>("Female", nameAndGenderArr[0]));
                }
            }

            line = lastNamesBr.readLine();

            // Get last names data
            while ((line = lastNamesBr.readLine()) != null) {
                String[] lastNamesArr = line.split(",");
                lastNames.add(lastNamesArr[0]);
            }

            line = populationBr.readLine();
            int resultantIterations = 0;

            // Get population data
            while ((line = populationBr.readLine()) != null) {
                String[] nationAndPercentageArr = line.split(",");
                double percentageRep = Double.parseDouble(nationAndPercentageArr[2]);

                iterationsPerNation.add(new Pair<Integer, String>((int) (recordsNum*percentageRep), nationAndPercentageArr[0]));
                resultantIterations += recordsNum*percentageRep;
            }

            // Assure that the total iterations are correct putting all the error on the greatest nation

            int greaterIterations = iterationsPerNation.get(0).getKey();
            String greatestNation = iterationsPerNation.get(0).getValue();

            if (resultantIterations > recordsNum) {
                iterationsPerNation.set(0, new Pair<>(greaterIterations - (resultantIterations - recordsNum), greatestNation));
            } else {
                iterationsPerNation.set(0, new Pair<>(greaterIterations + (recordsNum - resultantIterations), greatestNation));
            }

            // Once the load of all the data is done, start simulating the generation
            int nationCounter = 0;
            int resultantIterationsPerAge = 0;

            // Firstly, iterate depending on nation
            while (nationCounter < iterationsPerNation.size()) {
                Pair<Integer, String> actualNation = iterationsPerNation.get(nationCounter);

                int ageGroup1Iterations = (int) (percentagePerAgeGroup.get(0).getKey() * actualNation.getKey());
                int ageGroup2Iterations = (int) (percentagePerAgeGroup.get(1).getKey() * actualNation.getKey());
                int ageGroup3Iterations = (int) (percentagePerAgeGroup.get(2).getKey() * actualNation.getKey());
                int ageGroup4Iterations = (int) (percentagePerAgeGroup.get(3).getKey() * actualNation.getKey());
                int ageGroup5Iterations = (int) (percentagePerAgeGroup.get(4).getKey() * actualNation.getKey());

                // Assure that the total iterations are correct putting all the error on the first age group

                resultantIterationsPerAge = ageGroup1Iterations + ageGroup2Iterations + ageGroup3Iterations + ageGroup4Iterations + ageGroup5Iterations;

                if (resultantIterationsPerAge > iterationsPerNation.get(nationCounter).getKey()) {
                    ageGroup1Iterations = ageGroup1Iterations - (resultantIterationsPerAge - iterationsPerNation.get(nationCounter).getKey());
                } else {
                    ageGroup1Iterations = ageGroup1Iterations + (iterationsPerNation.get(nationCounter).getKey() - resultantIterationsPerAge);
                }

                // Secondly, iterate depending on age groups
                for (int j = 0; j < ageGroup1Iterations; j++) {
                    Person newPerson = null;

                    // Represent gender proportionally
                    // Boys
                    if (j < ageGroup1Iterations/2) {
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(0), actualNation.getValue(), genderAndNames, "Male", lastNames);
                    } else { // Girls
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(0), actualNation.getValue(), genderAndNames, "Female", lastNames);
                    }

                    fullNameAVLTree.insert(newPerson);
                    nameAVLTree.insert(newPerson);
                    lastNameAVLTree.insert(newPerson);
                    codeAVLTree.insert(newPerson);

                    recordsCounter++;
                    System.out.println(recordsCounter);
                }

                for (int j = 0; j < ageGroup2Iterations; j++) {
                    Person newPerson;

                    // Represent gender proportionally
                    // Boys
                    if (j < ageGroup1Iterations/2) {
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(1), actualNation.getValue(), genderAndNames, "Male", lastNames);
                    } else { // Girls
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(1), actualNation.getValue(), genderAndNames, "Female", lastNames);
                    }

                    fullNameAVLTree.insert(newPerson);
                    nameAVLTree.insert(newPerson);
                    lastNameAVLTree.insert(newPerson);
                    codeAVLTree.insert(newPerson);

                    recordsCounter++;
                    System.out.println(recordsCounter);
                }

                for (int j = 0; j < ageGroup3Iterations; j++) {
                    Person newPerson;

                    // Represent gender proportionally
                    // Boys
                    if (j < ageGroup1Iterations/2) {
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(2), actualNation.getValue(), genderAndNames, "Male", lastNames);
                    } else { // Girls
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(2), actualNation.getValue(), genderAndNames, "Female", lastNames);
                    }

                    fullNameAVLTree.insert(newPerson);
                    nameAVLTree.insert(newPerson);
                    lastNameAVLTree.insert(newPerson);
                    codeAVLTree.insert(newPerson);

                    recordsCounter++;
                    System.out.println(recordsCounter);
                }

                for (int j = 0; j < ageGroup4Iterations; j++) {
                    Person newPerson;

                    // Represent gender proportionally
                    // Boys
                    if (j < ageGroup1Iterations/2) {
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(3), actualNation.getValue(), genderAndNames, "Male", lastNames);
                    } else { // Girls
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(3), actualNation.getValue(), genderAndNames, "Female", lastNames);
                    }

                    fullNameAVLTree.insert(newPerson);
                    nameAVLTree.insert(newPerson);
                    lastNameAVLTree.insert(newPerson);
                    codeAVLTree.insert(newPerson);

                    recordsCounter++;
                    System.out.println(recordsCounter);
                }

                for (int j = 0; j < ageGroup5Iterations; j++) {
                    Person newPerson = null;

                    // Represent gender proportionally
                    // Boys
                    if (j < ageGroup1Iterations/2) {
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(4), actualNation.getValue(), genderAndNames, "Male", lastNames);
                    } else { // Girls
                        newPerson = generateRandomPerson(percentagePerAgeGroup.get(4), actualNation.getValue(), genderAndNames, "Female", lastNames);
                    }

                    fullNameAVLTree.insert(newPerson);
                    nameAVLTree.insert(newPerson);
                    lastNameAVLTree.insert(newPerson);
                    codeAVLTree.insert(newPerson);

                    recordsCounter++;
                    System.out.println(recordsCounter);
                }

                nationCounter++;
            }

            // Generate the preorder arrays after the generation of logs is finished
            generatePreorderArrays();

        } catch (IOException | DuplicateValueException e) {
            e.printStackTrace();
        }
    }

    public int generateCode() {
        int num = 0;
        Random random = new Random();

        do {
            num = random.nextInt(recordsNum * 2)+1;
        } while (codeSet.contains(num));

        codeSet.add(num);

        return num;
    }

    /**
     * Generates a random person with the provided data
     * @param percentagePerAgeGroup the percentage representation per age group
     * @param actualNation the nationality of the person
     * @param genderAndNames a list of various names with genders
     * @param selectedGender the selected gender of the person
     * @param lastNames a list of various lastnames
     * @return a random person
     */
    public Person generateRandomPerson(Pair<Double, Pair<Integer, Integer>> percentagePerAgeGroup, String actualNation, ArrayList<Pair<String, String>> genderAndNames,
                                              String selectedGender, ArrayList<String> lastNames){
        Random random = new Random();

        Pair<String, String> genderAndNameSelection;

        int minAge = percentagePerAgeGroup.getValue().getKey();
        int maxAge = percentagePerAgeGroup.getValue().getValue();
        int age = random.nextInt((maxAge + 1) - minAge) + minAge;
        LocalDate dateOfBirth = LocalDate.now().minusDays(random.nextInt(200)).minusYears(age);

        int code = generateCode();
        double height = (random.nextInt(50) + 150) * 0.01;

        // Boys
        if (selectedGender.equals("Male")) {
            genderAndNameSelection = genderAndNames.get(random.nextInt(3437));
        } else { // Girls
            genderAndNameSelection = genderAndNames.get(random.nextInt(3344) + 3437);
        }

        String name = genderAndNameSelection.getValue();
        String lastName = lastNames.get(random.nextInt(162253));
        String fullName = name + " " + lastName;
        String gender = genderAndNameSelection.getKey();

        return new Person(fullName, gender, dateOfBirth, height, actualNation, code);
    }

    /**
     * Generates the preorder arrays with the AVL Tree info provided
     */
    public void generatePreorderArrays(){
        fullNameAVLLogs = fullNameAVLTree.generatePreorderArray();
        nameAVLLogs = nameAVLTree.generatePreorderArray();
        lastNameAVLLogs = lastNameAVLTree.generatePreorderArray();
        codeAVLLogs = codeAVLTree.generatePreorderArray();
    }

    /**
     * Initializes the AVL Trees after the logs have been loaded from the JSON file
     */
    public void initializeTreesWithSavedLogs(){
        try {
            for (Person p :
                    fullNameAVLLogs) {
                fullNameAVLTree.insert(p);
            }

            for (Person p :
                    nameAVLLogs) {
                nameAVLTree.insert(p);
            }

            for (Person p :
                    lastNameAVLLogs) {
                lastNameAVLTree.insert(p);
            }

            for (Person p :
                    codeAVLLogs) {
                codeAVLTree.insert(p);
            }

        } catch (DuplicateValueException e) {
            e.printStackTrace();
        }
    }

    public void saveJSON() {
        try {
            Gson gson = new Gson();

            String json = gson.toJson(fullNameAVLLogs);
            File file = new File("data/logsFullName.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes());
            fos.close();

            String json2 = gson.toJson(nameAVLLogs);
            File file2 = new File("data/logsName.json");
            FileOutputStream fos2 = new FileOutputStream(file2);
            fos2.write(json2.getBytes());
            fos2.close();

            String json3 = gson.toJson(lastNameAVLLogs);
            File file3 = new File("data/logsLastName.json");
            FileOutputStream fos3 = new FileOutputStream(file3);
            fos3.write(json3.getBytes());
            fos3.close();

            String json4 = gson.toJson(codeAVLLogs);
            File file4 = new File("data/logsCode.json");
            FileOutputStream fos4 = new FileOutputStream(file4);
            fos4.write(json4.getBytes());
            fos4.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadJSON() {
        loadJSON("data/logsFullName.json", 1);
        loadJSON("data/logsName.json", 2);
        loadJSON("data/logsLastName.json", 3);
        loadJSON("data/logsCode.json", 4);

        initializeTreesWithSavedLogs();
    }

    private void loadJSON(String path, int logsOption) {
        try{
            FileInputStream fis = new FileInputStream(new File(path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String json = "";
            String line;

            while((line = reader.readLine()) != null) {
                json += line;
            }

            Gson gson = new Gson();
            Person[] data = gson.fromJson(json, Person[].class);

            if (data != null) {
                switch (logsOption) {
                    case 1:
                        fullNameAVLLogs.addAll(Arrays.asList(data));
                        break;
                    case 2:
                        nameAVLLogs.addAll(Arrays.asList(data));
                        break;
                    case 3:
                        lastNameAVLLogs.addAll(Arrays.asList(data));
                        break;
                    case 4:
                        codeAVLLogs.addAll(Arrays.asList(data));
                        break;
                }

                recordsNum = fullNameAVLLogs.size();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
