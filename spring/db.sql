--
-- Name: inventory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE inventory (
    inv_id bigint NOT NULL,
    item_id bigint,
    qty integer
);


ALTER TABLE inventory OWNER TO postgres;

--
-- Name: inventory_inv_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE inventory_inv_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE inventory_inv_id_seq OWNER TO postgres;

--
-- Name: inventory_inv_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE inventory_inv_id_seq OWNED BY inventory.inv_id;

--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE product (
    item_id bigint NOT NULL,
    description character varying(2000),
    name character varying(255),
    price double precision NOT NULL
);


ALTER TABLE product OWNER TO postgres;

--
-- Name: product_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE product_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE product_item_id_seq OWNER TO postgres;

--
-- Name: product_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE product_item_id_seq OWNED BY product.item_id;

--
-- Name: inventory inv_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inventory ALTER COLUMN inv_id SET DEFAULT nextval('inventory_inv_id_seq'::regclass);


--
-- Name: product item_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY product ALTER COLUMN item_id SET DEFAULT nextval('product_item_id_seq'::regclass);


--
-- Name: inventory inventory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inventory
    ADD CONSTRAINT inventory_pkey PRIMARY KEY (inv_id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (item_id);


--
-- Import product table data
--

insert into PRODUCT (item_id, name, description, price) values (329299, 'Red Fedora', 'Official Red Hat Fedora', 34.99);
insert into PRODUCT (item_id, name, description, price) values (329199, 'Forge Laptop Sticker', 'JBoss Community Forge Project Sticker', 8.50);
insert into PRODUCT (item_id, name, description, price) values (165613, 'Solid Performance Polo', 'Moisture-wicking, antimicrobial 100% polyester design wicks for life of garment. No-curl, rib-knit collar; special collar band maintains crisp fold; three-button placket with dyed-to-match buttons; hemmed sleeves; even bottom with side vents; Import. Embroidery. Red Pepper.',17.80);
insert into PRODUCT (item_id, name, description, price) values (165614, 'Ogio Caliber Polo', 'Moisture-wicking 100% polyester. Rib-knit collar and cuffs; Ogio jacquard tape inside neck; bar-tacked three-button placket with Ogio dyed-to-match buttons; side vents; tagless; Ogio badge on left sleeve. Import. Embroidery. Black.', 28.75);
insert into PRODUCT (item_id, name, description, price) values (165954, '16 oz. Vortex Tumbler', 'Double-wall insulated, BPA-free, acrylic cup. Push-on lid with thumb-slide closure; for hot and cold beverages. Holds 16 oz. Hand wash only. Imprint. Clear.', 6.00);
insert into PRODUCT (item_id, name, description, price) values (444434, 'Pebble Smart Watch', 'Smart glasses and smart watches are perhaps two of the most exciting developments in recent years.', 24.00);
insert into PRODUCT (item_id, name, description, price) values (444435, 'Oculus Rift', 'The world of gaming has also undergone some very unique and compelling tech advances in recent years. Virtual reality, the concept of complete immersion into a digital universe through a special headset, has been the white whale of gaming and digital technology ever since Geekstakes Oculus Rift GiveawayNintendo marketed its Virtual Boy gaming system in 1995.Lytro',106.00 );
insert into PRODUCT (item_id, name, description, price) values (444436, 'Lytro Camera', 'Consumers who want to up their photography game are looking at newfangled cameras like the Lytro Field camera, designed to take photos with infinite focus, so you can decide later exactly where you want the focus of each image to be.', 44.30);


--
-- Import inventory table data
--

insert into INVENTORY (inv_id, item_id, qty) values (345452, 329299, 10);
insert into INVENTORY (inv_id, item_id, qty) values (264563, 329199, 30);
insert into INVENTORY (inv_id, item_id, qty) values (312313, 165613, 13);
insert into INVENTORY (inv_id, item_id, qty) values (234256, 165614, 21);
insert into INVENTORY (inv_id, item_id, qty) values (341657, 165954, 16);
insert into INVENTORY (inv_id, item_id, qty) values (243256, 444434, 93);
insert into INVENTORY (inv_id, item_id, qty) values (124556, 444435, 194);
insert into INVENTORY (inv_id, item_id, qty) values (435213, 444436, 5);
