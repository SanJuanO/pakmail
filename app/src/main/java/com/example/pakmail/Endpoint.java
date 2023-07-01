package com.example.pakmail;

public class Endpoint {

    //public static String URL = "https://recompensassuperauto.com";
    public static String URL = "https://admin.pakmailmetepec.com/";
    public static String api = "services/";
    public static String version = "";
    public static String URL_WEB_SERVICE = URL + api + version;

        public static String URL_MENSAJERO = URL_WEB_SERVICE + "mensajeros_crud.php";
        public static String URL_GUIA = URL_WEB_SERVICE + "solicitudes_crud.php";

    public static String UPDATE_USER = URL_WEB_SERVICE + "clientes.php";
    public static String CORRIDAS = URL_WEB_SERVICE + "corridas_diaAcciones.php";
    public static String ORDER = URL_WEB_SERVICE + "ordenCompraAcciones.php";
    public static String CUPON = URL_WEB_SERVICE + "descuentos_acciones.php";
    public static String TURISTICO = URL_WEB_SERVICE + "viajes_turisticos.php";
    public static String TURISTICOA = URL_WEB_SERVICE + "viajes_turisticos_acciones.php";

    public static String UNIDADES = URL_WEB_SERVICE + "unidades.php";
    public static String UBICACIONTRASPORTE = URL_WEB_SERVICE + "unidades_acciones.php";


    public static String LIST = URL_WEB_SERVICE + "listar_tarjetas.php";

    public static String URL_AUTHORIZE = URL_WEB_SERVICE + "authenticate";
    public static String URL_RECOVERY = URL_WEB_SERVICE + "recovery-request";
}
