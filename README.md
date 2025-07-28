# 🔐 Shamir Secret Share Reconstructor (Java)

This is a simple Java project to **reconstruct a secret** from JSON files that hold secret shares (e.g., from Google Secret Manager export or a Shamir's Secret Sharing system).

It reads JSON files containing shares, reconstructs the secret (by summing the share values), and prints the result.

---

## 📁 File Structure

```
shamir-reconstructor/
├── Main.java              # Main logic to parse JSON and reconstruct the secret
├── input1.json            # Example share file 1
├── input2.json            # Example share file 2
├── output.txt             # File where the reconstructed secret is written
├── README.md              # This file
└── libs/
    └── json-20210307.jar  # org.json library
```

---

## 📦 Dependencies

- **Java 8 or higher**
- [`org.json`](https://mvnrepository.com/artifact/org.json/json) library (included in `libs/` folder)

---

## 🧠 Logic Used

Each input JSON file has this structure:

```json
{
  "shares": [
    {
      "index": 1,
      "value": 123
    }
  ]
}
```

The program:
1. Loads two JSON files
2. Parses the `value` from each file
3. Adds them to reconstruct the secret
4. Saves the result in `output.txt`

---

## 🚀 How to Run

### 1. Compile

```bash
javac -cp ".;libs/json-20210307.jar" Main.java
```

### 2. Run

```bash
java -cp ".;libs/json-20210307.jar" Main
```

✅ **On successful run, `output.txt` will contain the reconstructed secret.**

---

## 📝 Example

### Input Files

**input1.json:**
```json
{
  "shares": [
    { "index": 1, "value": 123 }
  ]
}
```

**input2.json:**
```json
{
  "shares": [
    { "index": 2, "value": 200 }
  ]
}
```

### Output

**output.txt:**
```
Reconstructed Secret: 323
```

---

## 🔧 Platform Notes

- **Windows:** Use semicolon (`;`) as classpath separator
- **Linux/Mac:** Use colon (`:`) as classpath separator

### Linux/Mac Commands:
```bash
# Compile
javac -cp ".:libs/json-20210307.jar" Main.java

# Run
java -cp ".:libs/json-20210307.jar" Main
```

---

## 🚨 Important Security Note

This is a **simplified implementation** for educational purposes. Real Shamir's Secret Sharing involves polynomial interpolation, not simple addition. For production use, consider proper cryptographic libraries.

---

## 🧑‍💻 Author

**Divyansh Chaturvedi**  
GitHub: [@divyanshnext](https://github.com/divyanshnext)

---

## 📄 License

This project is open source and available under the MIT License.
