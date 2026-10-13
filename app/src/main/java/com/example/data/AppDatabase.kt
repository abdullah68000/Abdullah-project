package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.FarmConfigDao
import com.example.data.dao.GalleryDao
import com.example.data.dao.OrderDao
import com.example.data.dao.ProductDao
import com.example.data.dao.ReviewDao
import com.example.data.dao.SubscriptionDao
import com.example.data.entity.FarmConfigEntity
import com.example.data.entity.GalleryItemEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.SubscriptionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        SubscriptionEntity::class,
        ReviewEntity::class,
        GalleryItemEntity::class,
        FarmConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun reviewDao(): ReviewDao
    abstract fun galleryDao(): GalleryDao
    abstract fun farmConfigDao(): FarmConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rehmat_dairy_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        seedDatabase(database)
                    }
                }
            }
        }

        suspend fun seedDatabase(db: AppDatabase) {
            // Seed initial products if empty
            if (db.productDao().getProductCount() == 0) {
                val initialProducts = listOf(
                    ProductEntity(
                        name = "Fresh Cow Milk",
                        description = "100% pure, unadulterated, whole cow milk collected under strict cold-chain hygiene. Rich in natural A2 nutrients.",
                        price = 240.0,
                        unit = "Liter",
                        category = "Milk",
                        imageResName = "img_farm_hero",
                        isAvailable = true,
                        isFeatured = true
                    ),
                    ProductEntity(
                        name = "Desi Ghee (Pure Cow)",
                        description = "Traditional slow-cooked golden Pakistani desi ghee made from fresh cultured makhkhan. Irresistible aroma and pure taste.",
                        price = 2600.0,
                        unit = "KG",
                        category = "Desi Ghee",
                        imageResName = "img_dairy_products",
                        isAvailable = true,
                        isFeatured = true
                    ),
                    ProductEntity(
                        name = "Yogurt / Dahi (Khameeri)",
                        description = "Thick, naturally sweet and velvety traditional farm dahi made in terracotta style without gelatin or artificial thickeners.",
                        price = 280.0,
                        unit = "KG",
                        category = "Yogurt / Dahi",
                        imageResName = "img_dairy_products",
                        isAvailable = true,
                        isFeatured = true
                    ),
                    ProductEntity(
                        name = "Farm Fresh Butter (Makhkhan)",
                        description = "Hand-churned unsalted white dairy butter, rich in flavor and velvety smooth. Perfect for parathas and baking.",
                        price = 1450.0,
                        unit = "500g",
                        category = "Butter",
                        imageResName = "img_dairy_products",
                        isAvailable = true,
                        isFeatured = false
                    ),
                    ProductEntity(
                        name = "Pure Dairy Cream (Malai)",
                        description = "Freshly separated thick cow milk cream with natural sweetness. Rich topping for desserts, sheer khurma and morning tea.",
                        price = 650.0,
                        unit = "500g",
                        category = "Cream",
                        imageResName = "img_milking_hygiene",
                        isAvailable = true,
                        isFeatured = false
                    ),
                    ProductEntity(
                        name = "Fresh Farm Paneer",
                        description = "Soft, protein-packed fresh cottage cheese made exclusively from whole cow milk. Free of preservatives.",
                        price = 950.0,
                        unit = "500g",
                        category = "Other",
                        imageResName = "img_dairy_products",
                        isAvailable = true,
                        isFeatured = false
                    ),
                    ProductEntity(
                        name = "Pure Khoya / Mawa",
                        description = "Slowly simmered whole milk khoya, ideal for authentic Pakistani sweets, gulab jamun, and halwa.",
                        price = 1100.0,
                        unit = "500g",
                        category = "Other",
                        imageResName = "img_dairy_products",
                        isAvailable = true,
                        isFeatured = false
                    )
                )
                db.productDao().insertAll(initialProducts)
            }

            // Seed gallery items
            if (db.galleryDao().getGalleryCount() == 0) {
                val initialGallery = listOf(
                    GalleryItemEntity(
                        title = "Healthy Sahiwal & Holstein Cows",
                        category = "Cows",
                        description = "Our prize pedigree cows graze freely in green pastures with 24/7 veterinary supervision.",
                        imageResName = "img_farm_hero"
                    ),
                    GalleryItemEntity(
                        title = "Automated Clean Milking Parlor",
                        category = "Milking",
                        description = "Touchless, stainless steel automated milking systems guarantee zero contamination.",
                        imageResName = "img_milking_hygiene"
                    ),
                    GalleryItemEntity(
                        title = "Farm Fresh Product Line",
                        category = "Dairy Products",
                        description = "Pure milk, golden desi ghee, and organic makhkhan prepared with traditional reverence.",
                        imageResName = "img_dairy_products"
                    ),
                    GalleryItemEntity(
                        title = "Morning Fodder Feeding & Grazing",
                        category = "Daily Farm Activities",
                        description = "Balanced diet including organic lucerne, corn silage, and clean mineral-rich water.",
                        imageResName = "img_farm_hero"
                    ),
                    GalleryItemEntity(
                        title = "Calf Care & Nursery",
                        category = "Calves",
                        description = "Dedicated nursery pens with temperature monitoring and mother milk nutrition for calves.",
                        imageResName = "img_farm_hero"
                    ),
                    GalleryItemEntity(
                        title = "Cold-Chain Fleet & Packaging",
                        category = "Farm",
                        description = "Chilled insulated transport delivering milk from udder to your doorstep within 4 hours.",
                        imageResName = "img_milking_hygiene"
                    )
                )
                db.galleryDao().insertAll(initialGallery)
            }

            // Seed initial reviews
            if (db.reviewDao().getReviewCount() == 0) {
                val initialReviews = listOf(
                    ReviewEntity(
                        customerName = "Muhammad Tariq",
                        city = "Lahore (DHA)",
                        rating = 5,
                        reviewText = "MashaAllah, the milk quality of Rehmat Dairy Farm is unmatched. No water mixing, thick natural malai, and delivered chilled every morning at 6:30 AM!",
                        isApproved = true,
                        createdAt = System.currentTimeMillis() - 86400000L * 2
                    ),
                    ReviewEntity(
                        customerName = "Ayesha Farooq",
                        city = "Lahore (Gulberg)",
                        rating = 5,
                        reviewText = "Their Desi Ghee has the exact fragrance my grandmother used to make in the village. Very impressed with the purity and packaging.",
                        isApproved = true,
                        createdAt = System.currentTimeMillis() - 86400000L * 5
                    ),
                    ReviewEntity(
                        customerName = "Usman Ali",
                        city = "Lahore (Johar Town)",
                        rating = 5,
                        reviewText = "Switched to their monthly milk subscription 3 months ago. Never missed a morning. Truly 'Fresh Milk, Pure Trust'!",
                        isApproved = true,
                        createdAt = System.currentTimeMillis() - 86400000L * 10
                    ),
                    ReviewEntity(
                        customerName = "Dr. Bilal Sheikh",
                        city = "Lahore (Model Town)",
                        rating = 5,
                        reviewText = "Visited their farm near Raiwind Road myself. The hygiene standards and clean automated milking are commendable. Highly recommended.",
                        isApproved = true,
                        createdAt = System.currentTimeMillis() - 86400000L * 15
                    )
                )
                db.reviewDao().insertAll(initialReviews)
            }

            // Seed initial farm config
            if (db.farmConfigDao().getConfigCount() == 0) {
                val initialConfigs = listOf(
                    FarmConfigEntity("whatsapp_number", "+923001234567"),
                    FarmConfigEntity("contact_phone", "+923001234567"),
                    FarmConfigEntity("contact_email", "info@rehmatdairy.pk"),
                    FarmConfigEntity("farm_address", "Rehmat Dairy Farm, Raiwind Road, Farm Belt, Lahore, Pakistan"),
                    FarmConfigEntity("opening_hours", "5:00 AM - 9:00 PM (Daily)"),
                    FarmConfigEntity("delivery_charge", "100"),
                    FarmConfigEntity("free_delivery_threshold", "1500"),
                    FarmConfigEntity("admin_pin", "1234"),
                    FarmConfigEntity("facebook_url", "https://facebook.com/rehmatdairyfarm"),
                    FarmConfigEntity("instagram_url", "https://instagram.com/rehmatdairyfarm"),
                    FarmConfigEntity("tiktok_url", "https://tiktok.com/@rehmatdairyfarm"),
                    FarmConfigEntity("youtube_url", "https://youtube.com/@rehmatdairyfarm")
                )
                db.farmConfigDao().setAllConfigs(initialConfigs)
            }
        }
    }
}
