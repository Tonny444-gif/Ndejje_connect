package com.example.ndejjeconnect.data

/**
 * Single source of truth for Ndejje University's academic structure.
 * Contains Faculties, Levels, Courses, and their respective Course Units.
 */
object Academia {

    val levels = listOf(
        "Certificate",
        "Diploma",
        "Bachelor's Degree",
        "Postgraduate Diploma",
        "Master's Degree",
        "Doctor of Philosophy (PhD)"
    )

    val faculties = listOf(
        "Faculty of Science and Computing",
        "Faculty of Engineering and Survey",
        "Faculty of Business Administration and Management",
        "Faculty of Humanities and Education",
        "Faculty of Social Sciences and Arts",
        "Faculty of Environment and Agricultural Sciences",
        "Faculty of Health Sciences",
        "The Graduate School"
    )

    // Map Structure: Faculty -> Level -> Course Name -> List of Course Units
    val academicStructure: Map<String, Map<String, Map<String, List<String>>>> = mapOf(
        "Faculty of Science and Computing" to mapOf(
            "Bachelor's Degree" to mapOf(
                "Bachelor of Computer Science (BCS)" to listOf("Discrete Mathematics", "Data Structures", "Operating Systems", "Software Engineering", "Artificial Intelligence", "Database Systems", "Computer Networks"),
                "Bachelor of Information Technology (BIT)" to listOf("Information Systems", "System Analysis", "E-Commerce", "Web Development", "Network Security", "IT Project Management"),
                "Bachelor of Science in Software Engineering (BSSE)" to listOf("Software Requirements", "Software Design", "Software Testing", "Mobile App Development", "Cloud Computing"),
                "Bachelor of Science in Computer Networks & Cyber Security" to listOf("Network Protocols", "Cyber Security Fundamentals", "Ethical Hacking", "Cryptography", "Wireless Networks"),
                "Bachelor of Science in Data Science & AI" to listOf("Machine Learning", "Big Data Analytics", "Statistical Modeling", "Data Visualization", "Deep Learning"),
                "Bachelor of Business Computing & Information Management (BBC)" to listOf("Business Processes", "ERP Systems", "Decision Support Systems", "Management Information Systems"),
                "Bachelor of Information Science & Secretarial Studies (BIS)" to listOf("Records Management", "Information Retrieval", "Office Administration", "Library Science"),
                "Bachelor of Science in Sports Science (BSS)" to listOf("Anatomy", "Biomechanics", "Sports Psychology", "Nutrition", "Sports Management")
            ),
            "Diploma" to mapOf(
                "Diploma in Computer Science (DCS)" to listOf("Introduction to Programming", "Computer Hardware", "Basic Mathematics", "Database Basics"),
                "Diploma in Information Technology (DIT)" to listOf("Network Essentials", "PC Maintenance", "Web Basics", "Office Applications"),
                "Diploma in Science Laboratory Technology (DSLT)" to listOf("Lab Techniques", "Basic Chemistry", "Biology Fundamentals", "Safety Procedures")
            ),
            "Certificate" to mapOf(
                "HEC in Physical Sciences (Computing Track)" to listOf("Foundational Physics", "Basic Computing", "Pre-University Math"),
                "HEC in Biological Sciences (IT Track)" to listOf("Foundational Biology", "Intro to IT", "Academic Communication"),
                "Certificate in Information Technology (CIT)" to listOf("Introduction to Computers", "Word Processing", "Spreadsheets", "Internet Skills"),
                "Certificate in Computer Applications" to listOf("Office Productivity", "Digital Literacy", "Basics of OS"),
                "Certificate in Graphics Design" to listOf("Design Principles", "Photoshop Basics", "Illustrator Basics", "Layout & Color"),
                "Certificate in Software Development (Junior)" to listOf("Logic & Algorithms", "Basic Coding", "Version Control"),
                "Certificate in Network Installation" to listOf("Cabling Basics", "Router Config", "Network Topology"),
                "Certificate in Web Design & Hosting" to listOf("HTML/CSS", "Domain Management", "FTP & Hosting"),
                "Certificate in Mobile Phone Repair" to listOf("Hardware Diagnostics", "Soldering", "Software Flashing"),
                "Certificate in Data Entry & Management" to listOf("Typing Speed", "Database Entry", "Data Integrity"),
                "Certificate in Sports Coaching" to listOf("Coaching Ethics", "Training Drills", "Team Management"),
                "Certificate in Sports Nutrition" to listOf("Diet Planning", "Hydration", "Supplement Basics")
            ),
            "Master's Degree" to mapOf(
                "Master of Computer Science (MCS)" to listOf("Advanced Algorithms", "Research Methodology", "Distributed Systems", "Theory of Computation"),
                "MSc. in Information Systems" to listOf("IT Strategy", "Advanced Database Management", "Business Intelligence"),
                "MSc. in Information Technology" to listOf("Network Management", "IT Governance", "Security Architectures"),
                "Master of Sports Science and Management" to listOf("Advanced Biomechanics", "Sports Policy", "Financial Mgt in Sports"),
                "Master of Science in Sports & Exercise Psychology" to listOf("Clinical Sports Psychology", "Motor Learning", "Behavioral Science"),
                "MSc. in Statistics (Research Track)" to listOf("Multivariate Analysis", "Stochastic Processes", "Probability Theory")
            ),
            "Postgraduate Diploma" to mapOf(
                "PGD in Computer Science" to listOf("Computing Essentials", "Systems Programming", "Algorithms"),
                "PGD in Information Systems" to listOf("Systems Analysis", "E-Business", "Database Design"),
                "PGD in Information Technology" to listOf("IT Infrastructure", "Cyber Law", "Network Admin"),
                "PGD in Sports Science" to listOf("Exercise Physiology", "Sports Medicine Basics"),
                "PGD in Sports Nutrition and Management" to listOf("Performance Nutrition", "Sports Marketing"),
                "PGD in Physical Education & Sports Management" to listOf("Curriculum Dev in PE", "Facility Management")
            ),
            "Doctor of Philosophy (PhD)" to mapOf(
                "PhD in Computer Science (Focus on AI/Data Science)" to listOf("Doctoral Seminar", "Advanced Machine Learning", "Thesis Research"),
                "PhD in Information Technology" to listOf("Emerging Technologies", "Advanced IT Theory", "Doctoral Dissertation"),
                "PhD in Information Systems" to listOf("IS Philosophy", "Socio-Technical Systems"),
                "PhD in Industrial and Applied Mathematics" to listOf("Numerical Analysis", "Mathematical Modeling"),
                "PhD in Sports Science and Management" to listOf("Sports Leadership", "Innovation in Sports Science"),
                "PhD in Statistics and Data Analytics" to listOf("Big Data Theory", "Advanced Inference")
            )
        ),
        "Faculty of Engineering and Survey" to mapOf(
            "Bachelor's Degree" to mapOf(
                "Bachelor of Engineering (Civil) (BCE)" to listOf("Engineering Math", "Structural Analysis", "Geology", "Fluid Mechanics", "Hydraulics"),
                "Bachelor of Engineering (Electrical) (BEE)" to listOf("Circuit Theory", "Electromagnetics", "Digital Electronics", "Power Systems"),
                "Bachelor of Engineering (Mechanical) (BME)" to listOf("Thermodynamics", "Materials Science", "Machine Design", "Manufacturing Processes"),
                "Bachelor of Engineering (Geomatics/Surveying) (SUV)" to listOf("Cartography", "GIS", "GPS Tech", "Cadastral Surveying"),
                "Bachelor of Engineering (Biomedical)" to listOf("Bio-instrumentation", "Medical Imaging", "Clinical Engineering"),
                "Bachelor of Engineering (Chemical)" to listOf("Reaction Engineering", "Transport Phenomena", "Process Control")
            ),
            "Diploma" to mapOf(
                "Diploma in Civil and Building Engineering (DCE)" to listOf("Building Construction", "Technical Drawing", "Construction Math"),
                "Diploma in Electrical Engineering (DEE)" to listOf("Electrical Installation", "AC/DC Machines", "Electronics Basics"),
                "Diploma in Mechanical Engineering (DME)" to listOf("Workshop Technology", "Auto-mechanics Basics"),
                "Diploma in Water Engineering" to listOf("Hydrology Basics", "Water Treatment Basics")
            ),
            "Certificate" to mapOf(
                "HEC in Physical Sciences (Engineering Track)" to listOf("Pre-Engineering Physics", "Intro to Calculus", "Technical Literacy"),
                "Certificate in Electrical Installation (Part 1)" to listOf("Domestic Wiring", "Electrical Safety", "Tools & Equipment"),
                "Certificate in Motor Vehicle Mechanics" to listOf("Engine Basics", "Brake Systems", "Suspension Fundamentals"),
                "Certificate in Plumbing and Pipefitting" to listOf("Pipe Joining", "Sanitary Fittings", "Water Supply Basics"),
                "Certificate in Bricklaying and Concrete Practice" to listOf("Bonding", "Concrete Mixing", "Scaffolding"),
                "Certificate in Carpentry and Joinery" to listOf("Woodwork Joints", "Roofing Basics", "Furniture Making"),
                "Certificate in Welding and Metal Fabrication" to listOf("Arc Welding", "Gas Cutting", "Sheet Metal Work"),
                "Certificate in Land Surveying (Basics)" to listOf("Taping", "Leveling", "Compass Surveying"),
                "Certificate in AutoCAD and Engineering Drawing" to listOf("2D Drafting", "Isometric Drawing", "Print Reading"),
                "Certificate in Renewable Energy Systems (Solar)" to listOf("PV Panels", "Battery Banks", "Inverter Installation"),
                "Certificate in Water Engineering (Technician)" to listOf("Pump Repair", "Irrigation Basics"),
                "Certificate in Electronic Repair" to listOf("Component Testing", "Circuit Boards", "TV/Radio Repair")
            ),
            "Master's Degree" to mapOf(
                "MSc. in Construction and Project Management" to listOf("Project Appraisal", "Procurement Management", "Quality Assurance"),
                "MSc. in Civil Engineering (Specialized Track)" to listOf("Advanced Structures", "Traffic Engineering"),
                "MSc. in Electrical Engineering (Specialized Track)" to listOf("Smart Grids", "Advanced Telecoms"),
                "MSc. in Mechanical Engineering (Specialized Track)" to listOf("Robotics", "Energy Systems Analysis"),
                "MSc. in Surveying and Land Studies" to listOf("Advanced GIS", "Geodesy", "Land Law")
            ),
            "Postgraduate Diploma" to mapOf(
                "PGD in Construction and Project Management" to listOf("Site Management", "Construction Ethics"),
                "PGD in Electrical Engineering" to listOf("Industrial Electronics", "Power Generation"),
                "PGD in Civil Engineering" to listOf("Structural Design Basics", "Quantity Surveying"),
                "PGD in Mechanical Engineering" to listOf("Applied Thermodynamics", "Fluid Power"),
                "PGD in Surveying and Mapping" to listOf("Photogrammetry", "Remote Sensing"),
                "PGD in Renewable Energy Systems" to listOf("Bio-energy", "Wind Power Systems"),
                "PGD in Engineering Management" to listOf("Operations Research", "Leadership for Engineers")
            ),
            "Doctor of Philosophy (PhD)" to mapOf(
                "PhD in Civil Engineering (Specialized Research)" to listOf("Research Proposal", "Doctoral Seminar"),
                "PhD in Electrical Engineering (Power Systems/Telecommunications)" to listOf("Advanced Signal Processing", "Grid Stability"),
                "PhD in Mechanical Engineering (Manufacturing/Energy)" to listOf("Precision Engineering", "Sustainable Energy"),
                "PhD in Construction Management" to listOf("Construction Innovation", "Project Governance"),
                "PhD in Surveying and Geomatics" to listOf("Geodynamics", "Big Spatial Data"),
                "PhD in Renewable Energy Systems" to listOf("Energy Policy", "Microgrid Tech")
            )
        ),
        "Faculty of Business Administration and Management" to mapOf(
            "Bachelor's Degree" to mapOf(
                "Bachelor of Business Administration (BBA - Accounting/Marketing/Mgt)" to listOf("Management Principles", "Accounting I", "Marketing", "Business Ethics"),
                "Bachelor of Commerce (COM)" to listOf("Macroeconomics", "Commerce", "Financial Management"),
                "Bachelor of Procurement & Logistics Management (BPL)" to listOf("Procurement Law", "Supply Chain Management", "Logistics"),
                "Bachelor of Human Resource Management (BHR)" to listOf("Organizational Behavior", "Labor Law", "Recruitment"),
                "Bachelor of Science in Accounting & Finance (BAF)" to listOf("Taxation", "Auditing", "Cost Accounting"),
                "Bachelor of Science in Banking and Insurance (BBI)" to listOf("Commercial Banking", "Risk Management", "Insurance Law"),
                "Bachelor of Entrepreneurship Management (BEM)" to listOf("Innovation", "Small Business Finance", "Venture Capital"),
                "Bachelor of Statistics and Planning (BSP)" to listOf("Probability", "Economic Planning", "Demography"),
                "Bachelor of Economics (BEC)" to listOf("Microeconomics", "Econometrics", "Development Economics"),
                "Bachelor of Hospitality and Tourism Management (BHT)" to listOf("Hotel Management", "Travel Agencies", "Eco-tourism")
            ),
            "Diploma" to mapOf(
                "Diploma in Business Administration (DBA)" to listOf("Office Management", "Bookkeeping", "Communication Skills")
            ),
            "Certificate" to mapOf(
                "HEC in Business Administration" to listOf("Intro to Business", "Pre-University Economics", "Quantitative Skills"),
                "Certificate in Business Administration (CBA)" to listOf("Business Communication", "Customer Relations", "Basic Mgt"),
                "Certificate in Accounting and Finance (CAF)" to listOf("Simple Bookkeeping", "Cash Management", "Petty Cash"),
                "Certificate in Procurement and Logistics" to listOf("Storekeeping Basics", "Inventory Entry", "Delivery Tracking"),
                "Certificate in Marketing and Sales" to listOf("Sales Pitch", "Customer Service", "Product Knowledge"),
                "Certificate in Human Resource Management" to listOf("Payroll Basics", "Records Entry", "Staff Motivation"),
                "Certificate in Secretarial and Office Management" to listOf("Shorthand", "Front Desk Mgt", "Filling Systems"),
                "Certificate in Banking and Microfinance" to listOf("Teller Duties", "Credit Basics", "Saving Schemes"),
                "Certificate in Records and Information Management" to listOf("Archiving", "Digital Records", "Confidentiality"),
                "Certificate in Entrepreneurship and Small Business" to listOf("Business Plan Basics", "Start-up Finance"),
                "Certificate in Project Planning (Foundation)" to listOf("Project Lifecycle", "Scheduling", "Resource Planning"),
                "Certificate in Public Relations" to listOf("Corporate Image", "Media Interaction", "Speech Writing")
            ),
            "Master's Degree" to mapOf(
                "Master of Business Administration (MBA) – Accounting" to listOf("Corporate Finance", "Strategic Management", "Forensic Accounting"),
                "MBA – Management" to listOf("Leadership", "Strategic HRM", "Global Business"),
                "MBA – Marketing" to listOf("Consumer Behavior", "Digital Marketing Strategy"),
                "MBA – Oil and Gas Management" to listOf("Energy Policy", "Oil & Gas Law", "Petroleum Economics"),
                "MSc. in Accounting and Finance" to listOf("Financial Reporting", "Investment Analysis"),
                "MSc. in Procurement and Supply Chain Management" to listOf("Global Logistics", "Contract Management"),
                "MSc. in Human Resource Management" to listOf("Talent Management", "Industrial Relations"),
                "MSc. in Project Management" to listOf("Project Risks", "M&E Systems"),
                "Master of Economics" to listOf("Advanced Microeconomics", "Advanced Econometrics")
            ),
            "Postgraduate Diploma" to mapOf(
                "PGD in Monitoring and Evaluation" to listOf("M&E Frameworks", "Data Collection", "Reporting"),
                "PGD in Human Resource Management" to listOf("HR Metrics", "Conflict Resolution"),
                "PGD in Oil and Gas Management" to listOf("Intro to Petroleum", "Health & Safety in O&G")
            ),
            "Doctor of Philosophy (PhD)" to mapOf(
                "PhD in Business Administration (Management)" to listOf("Organizational Theory", "Strategic Thinking"),
                "PhD in Business Administration (Accounting & Finance)" to listOf("Financial Theory", "Empirical Finance"),
                "PhD in Human Resource Management" to listOf("Strategic IHRM", "HR Research Seminar"),
                "PhD in Procurement and Supply Chain Management" to listOf("Supply Chain Philosophy", "Sustainable Procurement"),
                "PhD in Entrepreneurship and Innovation" to listOf("Innovation Ecosystems", "Social Entrepreneurship"),
                "PhD in Strategic Management" to listOf("Global Strategy", "Corporate Governance")
            )
        ),
        "Faculty of Humanities and Education" to mapOf(
            "Bachelor's Degree" to mapOf(
                "Bachelor of Arts with Education (BAED)" to listOf("Philosophy of Education", "Psychology of Learning", "Methods of Teaching"),
                "Bachelor of Science with Education (BSCED)" to listOf("Subject Methods", "School Practice", "Educational Planning"),
                "Bachelor of Early Childhood Education (BECE)" to listOf("Infant Care", "Curriculum for Pre-school", "Child Health"),
                "Bachelor of Education (Primary/Secondary) (BED)" to listOf("Pedagogy", "Measurement & Evaluation", "Inclusive Education"),
                "Bachelor of Teacher Technical Education (BTTE - Civil/Elec/Mech)" to listOf("Technical Drawing Methods", "Workshop Mgt"),
                "Bachelor of Theology (BTH)" to listOf("Systematic Theology", "Biblical Hebrew", "Church History"),
                "Bachelor of Biblical Studies & Christian Leadership (BSL)" to listOf("Leadership Ethics", "Old Testament", "New Testament")
            ),
            "Diploma" to mapOf(
                "Diploma in Primary Education (DPE)" to listOf("Early Childhood", "Primary Pedagogy"),
                "Diploma in Early Childhood Education (DECE)" to listOf("Child Development", "Play-based Learning"),
                "Diploma in Educational Institution Management (DEIM)" to listOf("School Finance", "Human Resource in Schools")
            ),
            "Certificate" to mapOf(
                "HEC in Humanities (Education Track)" to listOf("Academic Writing", "Intro to Pedagogy", "Pre-University History"),
                "Certificate in Early Childhood Development" to listOf("Child Psychology Basics", "Preschool Activities"),
                "Certificate in Nursery Teaching" to listOf("Classroom Management", "Phonics", "Lesson Planning"),
                "Certificate in Guidance and Counseling" to listOf("Counseling Skills", "Ethics in Counseling", "Mental Health Basics"),
                "Certificate in Theology" to listOf("Bible Study Basics", "Ministry Skills"),
                "Certificate in Christian Leadership" to listOf("Church Governance", "Spiritual Growth"),
                "Certificate in English Language Proficiency" to listOf("Grammar", "Composition", "Oral Communication"),
                "Certificate in Adult Education Techniques" to listOf("Andragogy", "Literacy Teaching", "Life Skills"),
                "Certificate in Library and Information Science" to listOf("Cataloging", "Library Admin", "Digital Libraries"),
                "Certificate in Educational Management" to listOf("Record Keeping", "School Governance"),
                "Certificate in Special Needs Education (Intro)" to listOf("Sign Language Basics", "Inclusive Methods"),
                "Certificate in Music and Creative Arts" to listOf("Notation", "Instrumental Basics", "Drama")
            ),
            "Master's Degree" to mapOf(
                "Master of Education (MED)" to listOf("Advanced Pedagogy", "Educational Research"),
                "Master of Education in Administration, Planning, and Management" to listOf("School Budgeting", "Policy Analysis"),
                "Master of Education in Curriculum Studies" to listOf("Curriculum Evaluation", "Instructional Design"),
                "Master of Educational Leadership" to listOf("Change Management", "Team Building"),
                "MA in Christian Religious Studies" to listOf("Advanced Theology", "World Religions")
            ),
            "Postgraduate Diploma" to mapOf(
                "PGD in Education (Secondary)" to listOf("Teaching Methods", "Classroom Interaction"),
                "PGD in Pedagogy" to listOf("Adult Learning Theory", "Instructional Technology"),
                "PGD in Early Childhood Education and Development" to listOf("Nursery Management", "Child Rights"),
                "PGD in Educational Institutional Management" to listOf("Resource Allocation", "Education Law"),
                "PGD in Higher Education Teaching" to listOf("HE Policy", "Assessment in HE"),
                "PGD in Special Needs Education" to listOf("Braille", "Intervention Strategies"),
                "PGD in Educational Leadership and Management" to listOf("Ethical Leadership", "Strategic Planning")
            ),
            "Doctor of Philosophy (PhD)" to mapOf(
                "PhD in Education (Educational Management & Planning)" to listOf("Global Trends in Education", "Leadership Theory"),
                "PhD in Education (Curriculum Studies)" to listOf("Curriculum Philosophy", "Theory of Instruction"),
                "PhD in Educational Leadership" to listOf("Executive Leadership", "Decision Making"),
                "PhD in Higher Education Management" to listOf("University Governance", "HE Finance"),
                "PhD in Christian Religious Studies" to listOf("Theological Research", "Biblical Interpretation"),
                "PhD in Language and Literature Education" to listOf("Applied Linguistics", "Literary Analysis")
            )
        ),
        "Faculty of Social Sciences and Arts" to mapOf(
            "Bachelor's Degree" to mapOf(
                "Bachelor of Public Administration & Management (BPA)" to listOf("Public Policy", "Administrative Law", "Governance"),
                "Bachelor of Social Work & Social Administration (BSW)" to listOf("Social Welfare", "Human Behavior", "Community Work"),
                "Bachelor of Journalism & Mass Communication (BJM)" to listOf("News Writing", "Media Ethics", "Broadcasting", "Photojournalism"),
                "Bachelor of Public Relations Management (BPR)" to listOf("PR Campaign", "Corporate Identity", "Crisis Mgt"),
                "Bachelor of Industrial Art & Design (BIAD)" to listOf("Graphics", "Painting", "Sculpture", "Textiles"),
                "Bachelor of Development Studies (BDS)" to listOf("Sustainable Dev", "Rural Development", "Poverty Analysis"),
                "Bachelor of Community Development (BCD)" to listOf("Community Mobilization", "Project Planning"),
                "Bachelor of Guidance and Counseling (BGC)" to listOf("Counseling Theories", "Psychopathology"),
                "Bachelor of Library and Information Sciences (BLIS)" to listOf("Information Organization", "Archival Mgt"),
                "Bachelor of Records and Information Management (BRIM)" to listOf("Electronic Records", "Knowledge Management")
            ),
            "Diploma" to mapOf(
                "Diploma in Journalism & Mass Communication (DJM)" to listOf("News Writing", "Media Ethics", "Broadcasting"),
                "Diploma in Commercial Art & Design (CAD)" to listOf("Drawing Basics", "Color Theory", "Commercial Graphics")
            ),
            "Certificate" to mapOf(
                "HEC in Humanities (Social Science Track)" to listOf("Intro to Sociology", "Pre-University Gov", "Study Skills"),
                "Certificate in Social Work and Social Administration" to listOf("Fieldwork Basics", "Social Welfare Intro"),
                "Certificate in Journalism and Media Studies" to listOf("Reporting 101", "Camera Handling", "Editing Basics"),
                "Certificate in Community Development" to listOf("Group Dynamics", "Mobilization Basics"),
                "Certificate in Public Administration" to listOf("Office Procedures", "Intro to Law"),
                "Certificate in International Relations (Intro)" to listOf("World History", "Diplomacy Basics"),
                "Certificate in Photography and Videography" to listOf("Lighting", "Post-production", "Composition"),
                "Certificate in Industrial Art and Design" to listOf("Basic Sketching", "Pottery", "Tie & Dye"),
                "Certificate in Fashion and Textile Design" to listOf("Pattern Drafting", "Tailoring", "Fabric Science"),
                "Certificate in Commercial Art" to listOf("Sign Writing", "Logo Design", "Illustration"),
                "Certificate in Peace and Conflict Management" to listOf("Mediation Basics", "Human Rights"),
                "Certificate in Administrative Law" to listOf("Constitutional Law", "Legal Systems")
            ),
            "Master's Degree" to mapOf(
                "MA in Public Administration and Management" to listOf("Public Sector Reform", "Local Government"),
                "MA in Development Studies" to listOf("Development Theory", "Project Management"),
                "MA in Peace, Human Rights, and Development" to listOf("International Law", "Conflict Resolution"),
                "MA in Gender and Development" to listOf("Gender Analysis", "Women in Dev"),
                "MA in Journalism and Multimedia Studies" to listOf("Digital Media", "Investigative Journalism"),
                "MA in Counseling Psychology" to listOf("Clinical Assessment", "Group Therapy"),
                "MA in Community Participation and Strategic Management" to listOf("Stakeholder Analysis", "NGO Mgt"),
                "Master of Public Relations Management" to listOf("Strategic PR", "Corporate Communication")
            ),
            "Postgraduate Diploma" to mapOf(
                "PGD in Guidance and Counseling" to listOf("Counseling Practice", "Psychological Testing"),
                "PGD in Public Administration" to listOf("Policy Implementation", "HR in Public Sector"),
                "PGD in Development Studies" to listOf("Sustainable Growth", "Project Mgt Basics"),
                "PGD in Community Participation" to listOf("Participatory Research", "Social Change")
            ),
            "Doctor of Philosophy (PhD)" to mapOf(
                "PhD in Public Administration and Management" to listOf("Governance Seminar", "Policy Theory"),
                "PhD in Development Studies" to listOf("Political Economy", "Global Dev Trends"),
                "PhD in Social Work and Social Administration" to listOf("Social Policy Analysis", "Welfare Theory"),
                "PhD in Peace and Conflict Studies" to listOf("Security Studies", "Peace Building"),
                "PhD in Counseling Psychology" to listOf("Advanced Psychotherapy", "Neuropsychology"),
                "PhD in Journalism and Multimedia Communication" to listOf("Media Philosophy", "Digital Culture")
            )
        ),
        "Faculty of Environment and Agricultural Sciences" to mapOf(
            "Bachelor's Degree" to mapOf(
                "Bachelor of Sustainable Agriculture & Extension (BSAE)" to listOf("Crop Science", "Animal Husbandry", "Soil Science", "Farm Power"),
                "Bachelor of Forest Science & Environmental Management (BFEM)" to listOf("Silviculture", "Ecology", "Forest Mapping", "Wildlife Mgt"),
                "Bachelor of Agricultural Entrepreneurship & Farm Management (BAEFM)" to listOf("Agribusiness", "Farm Accounting"),
                "Bachelor of Science in Cooperatives and Agribusiness (BCAM)" to listOf("Cooperative Law", "Agric Marketing"),
                "Bachelor of Science in Environmental Science" to listOf("Pollution Control", "Environmental Impact", "Geology"),
                "Bachelor of Science in Wood Science" to listOf("Wood Anatomy", "Timber Engineering", "Seasoning")
            ),
            "Diploma" to mapOf(
                "Diploma in Sustainable Agriculture and Extension (DSAE)" to listOf("Farm Management", "Agric Extension"),
                "Diploma in Animal Production and Extension (DAPE)" to listOf("Poultry Mgt", "Cattle Health"),
                "Diploma in Forestry" to listOf("Tree Nursery Mgt", "Forest Protection")
            ),
            "Certificate" to mapOf(
                "HEC in Biological Sciences (Agriculture Track)" to listOf("Pre-University Biology", "Agric Basics"),
                "National Certificate in Agriculture (NCA)" to listOf("Crop Husbandry", "Animal Health", "Agric Math"),
                "Certificate in Sustainable Farming" to listOf("Organic Farming", "Soil Conservation"),
                "Certificate in Animal Production" to listOf("Feeds & Feeding", "Breeding Basics"),
                "Certificate in Crop Protection" to listOf("Pest Management", "Herbicides Basics"),
                "Certificate in Forestry and Wood Science" to listOf("Tree Planting", "Log Processing"),
                "Certificate in Agribusiness Management" to listOf("Simple Bookkeeping", "Market Linkage"),
                "Certificate in Environmental Impact Assessment (Intro)" to listOf("EIA Process", "Nature Observation"),
                "Certificate in Bee Keeping (Apiculture)" to listOf("Hive Management", "Honey Extraction"),
                "Certificate in Poultry Management" to listOf("Broiler Management", "Layer Management"),
                "Certificate in Floriculture" to listOf("Flower Growing", "Greenhouse Basics"),
                "Certificate in Soil Science Basics" to listOf("Soil Testing", "Fertilizers")
            ),
            "Master's Degree" to mapOf(
                "Master of Sustainable Agriculture and Rural Development (MSARD)" to listOf("Food Security", "Rural Sociology"),
                "Master of Environmental and Natural Resources Management" to listOf("Environmental Law", "GIS for Environment"),
                "Master of Agribusiness Management" to listOf("Agric Supply Chain", "Agric Finance"),
                "MSc. in Integrated Watershed Management" to listOf("Hydrology", "Land Use Planning"),
                "MSc. in Disaster Risk Management" to listOf("Hazard Analysis", "Emergency Response"),
                "MSc. in Climate Change and Development" to listOf("Climate Policy", "Mitigation Strategies"),
                "MSc. in Forestry and Nature Conservation" to listOf("Tropical Forestry", "Biodiversity")
            ),
            "Postgraduate Diploma" to mapOf(
                "PGD in Environmental Management" to listOf("Waste Management", "Environmental Audit"),
                "PGD in Sustainable Agriculture" to listOf("Agro-ecology", "Seed Technology"),
                "PGD in Agribusiness Management" to listOf("Value Chain Analysis", "Farm Planning"),
                "PGD in Disaster Risk Management" to listOf("Early Warning Systems", "Disaster Recovery"),
                "PGD in Climate Change and Development" to listOf("Climate Adaptation", "Carbon Markets")
            ),
            "Doctor of Philosophy (PhD)" to mapOf(
                "PhD in Sustainable Agriculture and Rural Development" to listOf("Agriculture Innovation", "Rural Transformation"),
                "PhD in Environmental and Natural Resources Management" to listOf("Ecological Economics", "Resource Policy"),
                "PhD in Agribusiness Management" to listOf("Agric Markets Theory", "Strategic Agribusiness"),
                "PhD in Disaster Risk Management" to listOf("Resilience Theory", "Crisis Leadership"),
                "PhD in Climate Change and Development" to listOf("Climate Science", "Adaptation Policy"),
                "PhD in Soil Science and Crop Production" to listOf("Soil Microbiology", "Advanced Agronomy")
            )
        ),
        "Faculty of Health Sciences" to mapOf(
            "Bachelor's Degree" to mapOf(
                "Bachelor of Science in Nursing (BSN)" to listOf("Anatomy", "Physiology", "Nursing Fundamentals", "Pharmacology", "Surgical Nursing"),
                "Bachelor of Science in Public Health" to listOf("Epidemiology", "Biostatistics", "Environmental Health"),
                "Bachelor of Science in Midwifery" to listOf("Obstetrics", "Neonatal Care", "Reproductive Health"),
                "Bachelor of Medical Laboratory Technology" to listOf("Hematology", "Microbiology", "Clinical Chemistry"),
                "Bachelor of Science in Human Nutrition & Dietetics" to listOf("Clinical Nutrition", "Dietotherapy", "Food Science")
            ),
            "Diploma" to mapOf(
                "Diploma in Clinical Medicine & Community Health (DCMC)" to listOf("Clinical Diagnosis", "Pharmacology Basics"),
                "Diploma in Medical Laboratory Technology" to listOf("Lab Instruments", "Sample Collection"),
                "Diploma in Nursing" to listOf("Clinical Skills", "Community Health"),
                "Diploma in Midwifery" to listOf("Antenatal Care", "Postnatal Care")
            ),
            "Certificate" to mapOf(
                "HEC in Biological Sciences (Pre-Medical Track)" to listOf("Foundational Biology", "Intro to Chemistry", "Medical Literacy"),
                "Certificate in Nursing" to listOf("Patient Care", "Hygiene", "First Aid"),
                "Certificate in Midwifery" to listOf("Labor Support", "Infant Hygiene"),
                "Certificate in Theatre Techniques" to listOf("Sterilization", "Surgical Instruments"),
                "Certificate in Medical Records Management" to listOf("Patient Files", "Coding", "Confidentiality"),
                "Certificate in Laboratory Techniques (Foundation)" to listOf("Microscope Use", "Blood Sampling Basics"),
                "Certificate in Health Service Management" to listOf("Hospital Admin Basics", "Patient Flow"),
                "Certificate in Public Health (Community Track)" to listOf("Sanitation", "Immunization Awareness"),
                "Certificate in Nutrition and Dietetics (Intro)" to listOf("Balanced Diet", "Food Safety"),
                "Certificate in HIV/AIDS Counseling" to listOf("VCT Basics", "Stigma Reduction"),
                "Certificate in First Aid and Emergency Care" to listOf("CPR", "Trauma Handling", "Emergency Response"),
                "Certificate in Pharmacy Health Aide" to listOf("Drug Storage", "Dispensing Basics")
            )
        ),
        "The Graduate School" to mapOf(
            "Certificate" to mapOf(
                "Certificate in Project Monitoring and Evaluation" to listOf("M&E Tools", "Logframes", "Data Analysis"),
                "Certificate in Grant Writing" to listOf("Proposal Design", "Budgeting", "Donor Relations"),
                "Certificate in Strategic Leadership" to listOf("Decision Making", "Change Mgt", "Ethics"),
                "Certificate in Data Analysis (SPSS/Stata)" to listOf("Data Cleaning", "Descriptive Stats", "Inference"),
                "Certificate in Financial Management for Non-Finance Managers" to listOf("Balance Sheets", "Budget Control"),
                "Certificate in Human Resource Policy" to listOf("Policy Formulation", "Legal Compliance"),
                "Certificate in Supply Chain Risk Management" to listOf("Risk ID", "Mitigation Plans"),
                "Certificate in Quality Assurance in Education" to listOf("Standards", "Inspection Methods"),
                "Certificate in Cyber Security Awareness" to listOf("Phishing", "Password Security", "Digital Safety"),
                "Certificate in Disaster Preparedness" to listOf("Risk Mapping", "Community Readiness"),
                "Certificate in Gender Mainstreaming" to listOf("Gender Tools", "Inclusive Policy"),
                "Certificate in Research Methodology" to listOf("Research Design", "Literature Review")
            )
        )
    )

    /**
     * Helper function to get units for a specific course.
     */
    fun getUnits(faculty: String?, level: String?, course: String?): List<String> {
        if (faculty == null || level == null || course == null) return emptyList()
        return academicStructure[faculty]?.get(level)?.get(course) ?: emptyList()
    }

    /**
     * Helper function to get courses for a faculty and level.
     */
    fun getCourses(faculty: String?, level: String?): List<String> {
        if (faculty == null || level == null) return emptyList()
        return academicStructure[faculty]?.get(level)?.keys?.toList() ?: emptyList()
    }
}