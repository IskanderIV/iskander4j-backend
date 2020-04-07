db.createUser(
    {
        user: "clever_admin",
        pwd: "admin",
        roles: [
            {
                role: "readWrite",
                db: "cleverhause_devices_db"
            }
        ]
    }
);