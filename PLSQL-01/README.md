```bash

BEGIN
  DECLARE
    v_string1 VARCHAR2(30) := 'acbbcadefghkkhgfed';
    v_string2 VARCHAR2(30) := 'abbcddfggfca';
    v_char    VARCHAR2(1);
    v_output  VARCHAR2(4000);

    TYPE level_map IS TABLE OF PLS_INTEGER INDEX BY VARCHAR2(1);
    v_levels  level_map;

    v_current_level NUMBER := 0;
    v_prev_char     VARCHAR2(1) := NULL;
    i              NUMBER;

    PROCEDURE process_string(p_string IN VARCHAR2) IS
    BEGIN
      v_output := '';
      v_levels.DELETE;
      v_current_level := 0;
      v_prev_char := NULL;

      FOR i IN 1 .. LENGTH(p_string) LOOP
        v_char := SUBSTR(p_string, i, 1);

        IF v_levels.EXISTS(v_char) THEN
          -- Daha önce görüldüyse, kaydedilen seviyesine dön
          v_current_level := v_levels(v_char);
        ELSE
          -- Yeni karakter
          IF v_prev_char IS NULL OR (v_levels.EXISTS(v_prev_char) AND v_current_level = 0) THEN
            -- Yeni blok başı (örneğin ilk a veya d), seviye 1
            v_current_level := 1;
          ELSIF v_levels.EXISTS(v_prev_char) THEN
            -- Önceki karakterin seviyesine göre yeni seviye ata
            v_current_level := v_levels(v_prev_char) + 1;
          ELSE
            v_current_level := 1;
          END IF;

          v_levels(v_char) := v_current_level;
        END IF;

        -- Çıktıyı oluştur, seviye 1 için boşluk sıfır olacak
        v_output := v_output || RPAD(' ', (v_current_level - 1) * 2, ' ') || v_char || CHR(10);

        v_prev_char := v_char;
      END LOOP;

      -- Son satırdaki yeni satır karakterini kaldır
      v_output := RTRIM(v_output, CHR(10));
      DBMS_OUTPUT.PUT_LINE(v_output);
    END;

  BEGIN
    DBMS_OUTPUT.ENABLE;

    process_string(v_string1);
    process_string(v_string2);
  END;
END;

```


<img width="164" height="308" alt="image" src="https://github.com/user-attachments/assets/0cc0dabd-160e-453e-9b60-84a374167360" />

<img width="158" height="204" alt="image" src="https://github.com/user-attachments/assets/1df428a5-87b2-4c7b-844b-840c29082018" />
