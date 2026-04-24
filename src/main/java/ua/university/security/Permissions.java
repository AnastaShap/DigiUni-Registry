package ua.university.security;
public class Permissions {
    public static final int VIEW_ALL    = 1;      // 0001
    public static final int EDIT_DATA   = 2;      // 0010
    public static final int DELETE_DATA = 4;      // 0100 (має бути 4, а не 3!)
    public static final int ADMIN_FULL  = 8;      // 1000
}
