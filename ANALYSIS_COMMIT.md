Resumen del análisis rápido del proyecto Tesis-Angie

Fecha: 16 de septiembre de 2025

Hallazgos principales:

1. Seguridad: credenciales y contraseñas en texto plano
   - `RegisterActivity` guarda contraseñas en SharedPreferences sin cifrar.
   - `LoginActivity` usa credenciales hardcodeadas ("admin" / "1234").
   - Recomendación: usar EncryptedSharedPreferences o autenticación remota; eliminar credenciales hardcodeadas.

2. minSdk = 34
   - Actualmente limita la app a Android 14+. Revisar si es intencional. Si no, reducir minSdk para mayor compatibilidad.

3. Release sin minify/obfuscation
   - `isMinifyEnabled = false`. Habilitar R8 para builds de producción y revisar reglas ProGuard para librerías como Firebase/Retrofit.

4. Validación y sanitización de inputs
   - `SystemConfigurationActivity` y otras pantallas aceptan entradas sin validación estricta (URLs, números). Añadir validaciones.

5. Simulaciones y placeholders
   - Muchas actividades usan datos simulados (Random, sleeps) y placeholders. Planificar integrar APIs reales o aislar el código de demo.

Acciones recomendadas:
- Prioridad alta: Corregir almacenamiento de contraseñas, eliminar credenciales hardcodeadas.
- Prioridad media: Revisar minSdk, habilitar minify y añadir validaciones de inputs.
- Prioridad baja: Migrar a ViewBinding/Kotlin, añadir pruebas unitarias y configurar CI para build/lint.

Este commit añade sólo este archivo con el resumen del análisis. Si deseas que aplique cambios de código (p.ej. EncryptedSharedPreferences), dime y los implemento y commit.
