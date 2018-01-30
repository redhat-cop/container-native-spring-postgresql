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

