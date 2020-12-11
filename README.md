# MSA

MSA (Mikroservisne aplikacije) je projekat koji je razvijen sa ciljem demonstriranja upotrebe Spring framework-a, zajedno sa tehnologijama:
1. Spring Core/Boot/Cloud/Web
2. Osnove SQL-a - PostgreSQL
3. Osnove messaging-a - RabbitMQ
4. Docker
5. Jenkins
6. ELK
7. JUnit

## Plan projekta

1. Praćenje košarkaških utakmica
   1.1 Play-by-play servis - "servira" gotove podatke o utakmicama, kao da je u pitanju realan feed
   1.2 Game servis - periodično poziva play-by-play servis, snima podatke u pazu i šalje na message queue, simulirajući real-time feed
   1.3 Statistics servis - prima poruke sa message queue-a i računa statistike u realnom vremenu
2. Docker - "kontejnerizacija" svih servisa
3. Jenkins - automatizacija build i deploy procesa
4. ELK - agregacija logova svih servisa



