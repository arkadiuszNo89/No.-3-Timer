# No.-3-Timer

W aplikacji użytkownik tworzy listę wydarzeń określonych datą oraz czasem końcowym, po upływie którego następuje sygnalizacja.
Główne założenia aplikacji oraz próby kodu:
  - Dwie listy dynamiczne w GUI: jedna z wydarzeniami aktywnymi, druga archiwalnymi.
    (lista archiwalna ukazuje się po wciśnięciu przycisku "arch")
    (przycisk aktywny "arch" zmienia się na "add")
    (dynamicznie zmienia się również akcja przycisku "<" lub ">", który dodaje nowe wydarzenie lub przenosi zaznaczone do archiwum)
  - Przy użyciu obiektu klasy TimeLine regularnie aktywuję instancje własnego interfejsu (dodane są do listy interfejsów), w ten sposób          tworzę ich większą ilość, ich nadpisana różnorako metoda jest używana ze stałą częstotliwością.
  - Poza kontrolą końcowego czasu wydarzenia co minutę przeliczany jest procentowy progres na osi start - stop wydarzenia.
  - Wydarzenia rozdysponowują się na liście w kolejności od tego, które najszybciej się skończy.
  - Następuje rzutowanie procentowego progresu czterech pierwszych wydarzeń z listy aktywnej na ProgressIndicator (co minutę).
  - Dodatkowo progres zaznaczonego z listy aktywnej wydarzenia rzutowany zostaje na ProgressBar.
  - Save / Load dla wydarzeń do pliku tekstowego.
  - Zakończone wydarzenia dodane zostają do kolejki ArrayDeque oraz sygnalizowane są migający tłem Label'a z nazwą.
  - Po kliknięciu w aktywny Label po kolei ściągamy wydarzenia z kolejki widząc kolejno ich nazwy, aż alarm nie zniknie.
