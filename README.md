
# AutoStart - Sistem Integrat de Gestiune Parc Auto


AutoStart este o platformă destinată digitalizării operațiunilor dintr-un dealership auto. Aplicația gestionează fluxul complet: de la recepția vehiculelor în inventar și configurarea dotărilor opționale, până la managementul clienților și finalizarea contractelor de vânzare.

### Funcționalități principale

- Gestiunea stocului de mașini și a brandurilor
- Configurarea de dotări opționale pentru fiecare vehicul
- Managementul vânzărilor și al bazei de date de clienți
- Căutare și filtrare avansată pentru catalogul auto
- Monitorizarea performanței sistemului prin metrici centralizate

<img width="989" height="819" alt="c787f7f4-bd83-40bd-80f8-f5c68544c44d" src="https://github.com/user-attachments/assets/eb9c7e3c-ee69-4804-8b68-8dbae9d6f8bf" />


## Tech Stack
### Backend

- Spring Boot (microservicii), Spring Cloud (Eureka, Gateway, Config)

### Frontend

- React
### Baze de date

- PostgreSQL, H2

### Infrastructure

- Docker, Redis

# Product Backlog
## 1. Autentificare și Management Utilizatori
### User Story 1.1 - Înregistrare utilizator

**Ca** utilizator nou\
**Vreau** să pot crea un cont în aplicație\
**Pentru a** putea accesa funcționalitățile sistemului

**Criterii de acceptare:**

- Formular cu validare pentru email și parolă
- Email unic în sistem
- Date salvate în baza de date
- Mesaj de confirmare la finalizare

---

### User Story 1.2 - Autentificare / Logout

**Ca** utilizator existent\
**Vreau** să mă autentific în aplicație\
**Pentru a** accesa resursele corespunzătoare rolului meu

**Criterii de acceptare:**

- Login pe bază de email și parolă
- Generare token JWT
- JWT short-lived + refresh token
- Protejarea endpoint-urilor

---

### User Story 1.3 - Management profil utilizator

**Ca** utilizator autentificat\
**Vreau** să îmi pot modifica datele personale

**Criterii de acceptare:**

- Editare date personale
- Validare date introduse
- Persistență în baza de date

---

### User Story 1.4 - Management utilizatori (Admin)

**Ca** administrator\
**Vreau** să pot gestiona utilizatorii

**Criterii de acceptare:**

- Listă utilizatori
- Editare roluri
- Ștergere utilizatori



## 2. Management Vehicule

### User Story 2.1 - Administrare vehicule

**Ca** administrator\
**Vreau** să pot adăuga, modifica și șterge vehicule

**Criterii de acceptare:**

- CRUD complet pentru vehicule
- Validare date (preț, VIN - 17 caractere)
- Salvare în baza de date

---

### User Story 2.2 - Vizualizare inventar

**Ca** utilizator\
**Vreau** să pot vizualiza lista vehiculelor

**Criterii de acceptare:**

- Listă vehicule
- Filtrare (brand, preț)
- Căutare
- Paginare și sortare

---

### User Story 2.3 - Configurare opțiuni vehicul

**Ca** administrator\
**Vreau** să pot configura opțiunile unui vehicul

**Criterii de acceptare:**

- CRUD opțiuni
- Asociere Brand - opțiuni
- Persistență relații



## 3. Management Clienți

### User Story 3.1 - Administrare clienți

**Ca** administrator\
**Vreau** să pot gestiona clienții

**Criterii de acceptare:**

- CRUD clienți
- Asociere cu utilizator
- Validare date


## 4. Contracte și Vânzări

### User Story 4.1 - Creare contract

**Ca** agent de vânzări\
**Vreau** să creez un contract de vânzare

**Criterii de acceptare:**

- Selectare client și vehicul
- Calcul automat preț
- Salvare contract

---

### User Story 4.2 - Finalizare vânzare

**Ca** agent de vânzări\
**Vreau** să finalizez o vânzare

**Criterii de acceptare:**

- Actualizare status vehicul (SOLD)
- Actualizare status client
- Asociere contract finalizat

---

### User Story 4.3 - Gestionare plăți

**Ca** agent de vânzări\
**Vreau** să înregistrez plata aferentă unui contract

**Criterii de acceptare:**

- Asociere payment–contract
- Validare sumă
- Persistență în baza de date


## 5. Redis și Caching

### User Story 5.1 - Cache pentru vehicule

**Scop:** Reducerea timpului de răspuns

**Criterii de acceptare:**

- Cache pentru endpoint-uri
- Invalidare cache la modificări
- Performanță îmbunătățită

---

### User Story 5.2 - Cache pentru opțiuni

**Criterii de acceptare:**

- Cache pentru date statice
- Sincronizare cu baza de date



## 6. Arhitectură Microservicii și Infrastructură

### 6.1 Service Discovery (Eureka)

- Service registry funcțional
- Înregistrare automată servicii



### 6.2 API Gateway

- Routing centralizat
- Filtrare request/response



### 6.3 Config Server

- Configurații externalizate
- Refresh dinamic



### 6.4 Load Balancing

- Minimum 2 instanțe per serviciu
- Distribuire echilibrată a cererilor



### 6.5 Monitorizare și Metrici

- Spring Boot Actuator
- Prometheus + Grafana
- Health checks



### 6.6 Securitate (JWT)

- Generare și validare JWT
- Endpoint-uri securizate



### 6.7 Redis (Caching )

- Cache pentru endpoint-uri cheie
- Invalidare corectă a cache-ului

## Autori

Bărboi Sabin

Ichim Vlad
