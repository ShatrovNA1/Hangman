import java.util.Collections;

Random random = new Random();

enum State {
    MENU,
    GAME,
    EXIT,
}

List<String> getAllWords() {
    try (FileReader fileReader = new FileReader("dictionary.txt")) {
        return fileReader.readAllLines();
    } catch (Exception _) {
        System.out.println("Проблема с загрузкой словаря из файла");
        return Collections.emptyList();
    }
}

void printMenu() {
    IO.println();
    IO.println("====== ВИСЕЛИЦА ======");
    IO.println("1) Новая игра");
    IO.println("2) Выход");
    IO.println("======================");
    IO.println("Для выбора введите число:");
}

void printWord(String word, Set<Character> guessedLetters) {
    for (char c : word.toCharArray()) {
        if (guessedLetters.contains(c)) {
            IO.print(c + " ");
        } else {
            IO.print("_ ");
        }
    }
    IO.println();
}

String getRandomWord(List<String> allWords) {
    int index = random.nextInt(allWords.size());
    return allWords.get(index);
}

void printHangman(int attempts) {
    String[] hangman = {
            """
             +---+
             |   |
                 |
                 |
                 |
                 |
            =========
            """,

            """
             +---+
             |   |
             O   |
                 |
                 |
                 |
            =========
            """,

            """
             +---+
             |   |
             O   |
             |   |
                 |
                 |
            =========
            """,

            """
             +---+
             |   |
             O   |
            /|   |
                 |
                 |
            =========
            """,

            """
             +---+
             |   |
             O   |
            /|\\  |
                 |
                 |
            =========
            """,

            """
             +---+
             |   |
             O   |
            /|\\  |
            /    |
                 |
            =========
            """,

            """
             +---+
             |   |
             O   |
            /|\\  |
            / \\  |
                 |
            =========
            """
    };

    IO.println(hangman[Math.min(attempts, 6)]);
}

boolean isWordGuessed(String word, Set<Character> guessedLetters) {
    for (char c : word.toCharArray()) {
        if (!guessedLetters.contains(c)) {
            return false;
        }
    }

    return true;
}

void startGame(List<String> allWords) {
    String word = getRandomWord(allWords);
    Set<Character> guessedLetters = new HashSet<>();

    int attempts = 0;
    final int MAX_ATTEMPTS = 6;

    while (attempts < MAX_ATTEMPTS) {

        IO.println();
        IO.println("Количество ошибок: " + attempts + " из " + MAX_ATTEMPTS);

        printHangman(attempts);

        IO.print("Слово: ");
        printWord(word, guessedLetters);

        IO.print("Введите букву: ");
        String input = IO.readln().trim().toLowerCase();
        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            IO.println("Введите одну букву.");
            continue;
        }

        char letter = input.charAt(0);

        if (letter < 'а' || letter > 'я') {
            IO.println("Введите букву русского алфавита не заглавную");
            continue;
        }

        if (guessedLetters.contains(letter)) {
            IO.println("Вы уже вводили эту букву.");
            continue;
        }

        guessedLetters.add(letter);
        if (word.indexOf(letter) >= 0) {
            IO.println("Правильно!");
        } else {
            attempts++;
            IO.println("Такой буквы нет.");
        }

        if (isWordGuessed(word, guessedLetters)) {
            IO.println();
            IO.println("ПОБЕДА!");
            IO.println("Слово: " + word);
            return;
        }
    }

    printHangman(MAX_ATTEMPTS);

    IO.println();
    IO.println("Поражение!");
    IO.println("Загаданное слово: " + word);
}

void main() {

    List<String> allWords = getAllWords();

    if (allWords.isEmpty()) {
        return;
    }

    State gameState = State.MENU;

    while (gameState != State.EXIT) {
        switch (gameState) {
            case MENU -> {
                printMenu();
                String input = IO.readln();
                switch (input) {
                    case "1":
                        gameState = State.GAME;
                        break;
                    case "2":
                        gameState = State.EXIT;
                        break;
                    default:
                        IO.println("Неправильный ввод.");
                }
            }
            case GAME -> {
                startGame(allWords);
                gameState = State.MENU;
            }
            case EXIT -> IO.println("До свидания!");
        }
    }

    IO.println("До свидания!");
}