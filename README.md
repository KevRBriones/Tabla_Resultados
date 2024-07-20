# Formula 1 Statistics Application

![](https://scontent.fgye1-2.fna.fbcdn.net/v/t1.15752-9/450499378_1259813632065288_1007715998113363942_n.png?_nc_cat=103&ccb=1-7&_nc_sid=9f807c&_nc_eui2=AeETxPjNZkjQA73sdDnZwM758gEu--pDtqfyAS776kO2p63dxAcRepEnN1TBRLWDNzFz4jKMLX65tbzlV7rTsDLQ&_nc_ohc=OJzkAlvYlS0Q7kNvgHwY1_3&_nc_ht=scontent.fgye1-2.fna&oh=03_Q7cD1QFmMPywWxZlgD2ZAPP5xyUEN8zhfkRYXoTmolRxWl3YFg&oe=66C2AB4C)
![](https://scontent.fgye1-2.fna.fbcdn.net/v/t1.15752-9/450816719_3805786719666150_1358734866311674425_n.png?_nc_cat=105&ccb=1-7&_nc_sid=9f807c&_nc_eui2=AeGk0sJsv9PbUMEBZtDzmMhfYnMWU023p39icxZTTbenf9_NdSeKO2qp8XVBA3su_cda_mWLt6TG5sjVCzcK78AB&_nc_ohc=Mezm-WJOLGoQ7kNvgHoPsYP&_nc_ht=scontent.fgye1-2.fna&oh=03_Q7cD1QEEJXrUhLM8Q1idMRA4ucD3yk7Cp_2QkDTskIq7ur6YnA&oe=66C2A4F3)

## Descripción
Esta aplicación de JavaFX proporciona una interfaz gráfica para visualizar estadísticas de Fórmula 1. Permite a los usuarios explorar datos históricos de pilotos y constructores por año, mostrando información detallada y gráficos comparativos.

## Funcionalidades

1. **Selección de Año**
   - Permite al usuario seleccionar un año específico para visualizar estadísticas.
   - Rango de años disponibles: 2009-2023.

2. **Tabla de Pilotos**
   - Muestra estadísticas de pilotos para el año seleccionado.
   - Columnas: Nombre del Piloto, Victorias, Puntos Totales, Posición en el Campeonato.

3. **Tabla de Constructores**
   - Presenta estadísticas de constructores para el año seleccionado.
   - Columnas: Nombre del Constructor, Nacionalidad, Victorias, Puntos Totales, Posición en el Campeonato.

4. **Gráficos Comparativos**
   - Botón "Ver Gráficos" que abre una nueva ventana con gráficos de barras.
   - Gráfico de Pilotos: Compara los puntos totales de los 10 mejores pilotos.
   - Gráfico de Constructores: Compara los puntos totales de todos los constructores.

## Características Técnicas

- **Interfaz de Usuario**: Desarrollada con JavaFX.
- **Gestión de Datos**: Utiliza JDBC para conectarse a una base de datos SQL.
- **Visualización de Datos**: Emplea TableView para mostrar datos tabulares y BarChart para gráficos.
- **Diseño Modular**: Separa la lógica de la interfaz de usuario y el acceso a datos.

## Uso

1. Seleccione un año del menú desplegable.
2. Las tablas de pilotos y constructores se actualizarán automáticamente.
3. Haga clic en "Ver Gráficos" para abrir una ventana con gráficos comparativos.

## Requisitos

- Java Runtime Environment (JRE)
- JavaFX
- Conexión a la base de datos de Fórmula 1 (configurada en la clase `Main`)

## Notas de Implementación

- La aplicación utiliza consultas SQL complejas para calcular estadísticas como victorias y rankings.
- Los datos se obtienen dinámicamente de la base de datos para cada año seleccionado.
- Se implementa manejo de errores básico con alertas para el usuario.

## Posibles Mejoras

- Implementar filtros adicionales (por ejemplo, por equipo o nacionalidad).
- Añadir más tipos de gráficos y visualizaciones.
- Incluir opciones para exportar datos.
- Mejorar el rendimiento para grandes conjuntos de datos.
