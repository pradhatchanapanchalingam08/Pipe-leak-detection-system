package com.example.pipedetection.config;

import com.example.pipedetection.entity.*;
import com.example.pipedetection.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Creates sample data the first time the application starts, so the complete
 * demo workflow (drone -> leak -> repair -> invoice -> payment) can be tried
 * out immediately without manual setup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final DroneRepository droneRepository;
    private final AnalyticAccountRepository analyticAccountRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ContactRepository contactRepository,
                            ProductRepository productRepository,
                            AccountRepository accountRepository,
                            DroneRepository droneRepository,
                            AnalyticAccountRepository analyticAccountRepository,
                            SystemSettingRepository systemSettingRepository,
                            AppUserRepository appUserRepository,
                            PasswordEncoder passwordEncoder) {
        this.contactRepository = contactRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.droneRepository = droneRepository;
        this.analyticAccountRepository = analyticAccountRepository;
        this.systemSettingRepository = systemSettingRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedContacts();
        seedProducts();
        seedAccounts();
        seedDrones();
        seedAnalyticAccounts();
        seedSystemSettings();
        seedUsers();
    }

    private void seedContacts() {
        if (contactRepository.count() > 0) {
            return;
        }

        Contact waterBoard = new Contact();
        waterBoard.setName("City Water Board");
        waterBoard.setContactType(Contact.ContactType.MUNICIPAL_WATER_BOARD);
        waterBoard.setEmail("contact@citywaterboard.gov");
        waterBoard.setPhone("555-0100");
        waterBoard.setAddress("100 Civic Center Drive");
        contactRepository.save(waterBoard);

        Contact supplier = new Contact();
        supplier.setName("Industrial Sealant Supplier");
        supplier.setContactType(Contact.ContactType.SEALANT_SUPPLIER);
        supplier.setEmail("sales@industrialsealant.com");
        supplier.setPhone("555-0200");
        supplier.setAddress("200 Industrial Park Road");
        contactRepository.save(supplier);
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return;
        }

        Product repairService = new Product();
        repairService.setName("Ultrasonic Pipe Leak Repair");
        repairService.setType(Product.ProductType.SERVICE);
        repairService.setDescription("Automated ultrasonic sealing repair performed by a pipe crawler drone");
        repairService.setUnitPrice(new BigDecimal("450.00"));
        repairService.setStockQuantity(0);
        productRepository.save(repairService);

        Product sealant = new Product();
        sealant.setName("Epoxy Resin Sealant Cartridge");
        sealant.setType(Product.ProductType.GOODS);
        sealant.setDescription("Cartridge of epoxy resin used by the drone to seal detected leaks");
        sealant.setUnitPrice(new BigDecimal("25.00"));
        sealant.setStockQuantity(100);
        productRepository.save(sealant);

        Product servicing = new Product();
        servicing.setName("Pipe Crawler Servicing");
        servicing.setType(Product.ProductType.SERVICE);
        servicing.setDescription("Routine maintenance and servicing of a pipe crawler drone");
        servicing.setUnitPrice(new BigDecimal("120.00"));
        servicing.setStockQuantity(0);
        productRepository.save(servicing);
    }

    private void seedAccounts() {
        if (accountRepository.count() > 0) {
            return;
        }

        accountRepository.save(new Account("Pipe Crawler Drone Fleet", Account.AccountType.ASSET));
        accountRepository.save(new Account("Cash/Bank", Account.AccountType.ASSET));
        accountRepository.save(new Account("Sealant Supplier Payables", Account.AccountType.LIABILITY));
        accountRepository.save(new Account("Drone Component Creditors", Account.AccountType.LIABILITY));
        accountRepository.save(new Account("Pipe Repair Revenues", Account.AccountType.INCOME));
        accountRepository.save(new Account("Preventive Inspection Fees", Account.AccountType.INCOME));
        accountRepository.save(new Account("Sealant Material Costs", Account.AccountType.EXPENSE));
        accountRepository.save(new Account("Drone Battery Charging Expenses", Account.AccountType.EXPENSE));
    }

    private void seedDrones() {
        if (droneRepository.count() > 0) {
            return;
        }

        Drone drone1 = new Drone();
        drone1.setDroneCode("DRONE-001");
        drone1.setDroneName("Crawler Alpha");
        drone1.setStatus(Drone.DroneStatus.AVAILABLE);
        drone1.setBatteryLevel(100);
        drone1.setCurrentLocation("Depot A");
        droneRepository.save(drone1);

        Drone drone2 = new Drone();
        drone2.setDroneCode("DRONE-002");
        drone2.setDroneName("Crawler Beta");
        drone2.setStatus(Drone.DroneStatus.AVAILABLE);
        drone2.setBatteryLevel(95);
        drone2.setCurrentLocation("Depot B");
        droneRepository.save(drone2);
    }

    private void seedAnalyticAccounts() {
        if (analyticAccountRepository.count() > 0) {
            return;
        }

        AnalyticAccount sector = new AnalyticAccount();
        sector.setName("Water Main Zone West");
        sector.setDescription("Pipeline sector covering the western water main network");
        analyticAccountRepository.save(sector);
    }

    private void seedSystemSettings() {
        if (systemSettingRepository.count() > 0) {
            return;
        }

        SystemSetting settings = new SystemSetting();
        settings.setId(1L);
        settings.setLeakToleranceLimit(70.0);
        systemSettingRepository.save(settings);
    }
    private void seedUsers() {
        if (appUserRepository.count() > 0) {
            return;
        }
        appUserRepository.save(new AppUser(
                "admin", passwordEncoder.encode("admin123"), AppUser.Role.ADMIN, null, "Water Network Director"));
        appUserRepository.save(new AppUser(
                "customer", passwordEncoder.encode("customer123"), AppUser.Role.CUSTOMER, 1L, "City Water Board Customer"));
    }

}
