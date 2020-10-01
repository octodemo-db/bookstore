CREATE TABLE IF NOT EXISTS books (
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL UNIQUE,
    author CHARACTER(50),
    image TEXT,
    rating INTEGER,
    CONSTRAINT bounded_rating CHECK (rating >= 0 AND rating <= 5)
);