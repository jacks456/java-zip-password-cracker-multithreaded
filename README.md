# Multithreaded ZIP Password Cracker (Educational)

> **Purpose:** Demonstrate concurrent search, I/O isolation, timing/benchmarking, and clean shutdown patterns in Java using an educational ZIP password brute-force tool.

**Targets:** Early‑career systems/security/perf roles. Built from a Wright State University CS2 project brief (Project 4).

## ⚠️ Ethics & Usage
This code is for **learning** and **authorized testing** only. Use **only** on archives you own or have explicit permission to test. Do **not** use against others' data.

## ✨ Highlights
- **Work‑stealing/partitioned search** of keyspace across `N` threads
- **Early exit** on success using a shared flag/interrupt
- **Per‑thread temp workspace** to avoid I/O contention
- **Timing & benchmarking hooks** (compare 3 vs 4 threads)
- **Deterministic cleanup** of temp files/dirs

## 🧰 Tech
- Java 17+ (works on 11+ with minor tweaks)
- Standard library + `java.util.concurrent`
- Zip handling via `java.util.zip.*` or Apache Commons Compress (optional)

## 🚀 Quick Start
```bash
# Compile
javac -d out $(find src -name "*.java")

# Minimal run (example flags)
java -cp out edu.wsu.cs2.PasswordCracker   --zip ./samples/locked.zip   --charset "abc123"   --min-length 1   --max-length 5   --threads 4
```

**Common flags**
- `--zip` path to target `.zip`
- `--threads` parallelism (default: CPU cores)
- `--charset` candidate characters (e.g., `a-zA-Z0-9`)
- `--min-length`, `--max-length` bounds of the search

## 🧪 Reproducible Demo (make your own sample)
```bash
# Create a small password-protected zip for **authorized** testing
# On macOS/Linux (Info-ZIP):
zip -P a1b2C demo.zip README.md
# Now run the cracker with a candidate charset that contains a,b,1,2,C
```

Expected behavior:
- All workers start and enumerate candidate space
- First worker to crack the password sets a **shared stop signal**
- Main thread reports **elapsed time** and cleans temp paths

## 📊 Benchmarks (template)
Fill this in after running on your machine.

| Threads | Charset | Len Range | Elapsed (s) |
|--------:|---------|-----------|-------------|
| 3       | a…z0…9  | 1–5       | XX.X        |
| 4       | a…z0…9  | 1–5       | YY.Y        |

> Note: throughput scales sub‑linearly as I/O and branch mispredictions dominate for small keyspaces. Larger keyspaces typically show clearer gains.

## 🧱 Repo Structure
```
src/
  edu/wsu/cs2/
    PasswordCracker.java
    Worker.java
    Coordinator.java
    CandidateGenerator.java
    ZipProbe.java
    util/Timer.java
samples/           # (optional) place your own authorized zips here
scripts/           # helpers to generate demo archives
README.md
LICENSE
```

## ✅ Testing
- Unit tests for candidate generation and stop‑signal propagation
- Integration test using a small demo archive
- CI (GitHub Actions) compiles and runs unit tests

## 📝 Notes
- Avoid committing any course‑provided **answers or test vectors** verbatim.
- Keep instructor PDFs private unless your syllabus allows distribution.
- This repo includes **design commentary**: keyspace partitioning, thread coordination, and safe shutdown patterns.
