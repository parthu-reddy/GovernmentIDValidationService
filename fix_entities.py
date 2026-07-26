import os
import re

dir_path = "src/main/java/com/fooddelivery/governmentid"

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Remove DeliveryExecutive import
    content = re.sub(r'import com\.fooddelivery\.delivery\.entity\.DeliveryExecutive;\n', '', content)
    content = re.sub(r'import com\.fooddelivery\.delivery\.repository\.IDeliveryExecutiveRepository;\n', '', content)
    
    # Replace JPA relation with UUID
    content = re.sub(
        r'@ManyToOne.*?\n.*?@JoinColumn\(name\s*=\s*"executive_id".*?\)\n\s*private\s+DeliveryExecutive\s+executive;',
        '@Column(name = "executive_id", nullable = false)\n    private UUID executiveId;',
        content,
        flags=re.DOTALL
    )

    content = re.sub(
        r'@OneToOne.*?\n.*?@JoinColumn\(name\s*=\s*"executive_id".*?\)\n\s*private\s+DeliveryExecutive\s+executive;',
        '@Column(name = "executive_id", nullable = false)\n    private UUID executiveId;',
        content,
        flags=re.DOTALL
    )

    # In services, remove repository and executive dependencies
    content = re.sub(r'IDeliveryExecutiveRepository.*?deliveryExecutiveRepository;', '', content)
    content = re.sub(r'DeliveryExecutive\s+executive\s*=\s*deliveryExecutiveRepository\.findById\(executiveId\).*?;', '', content)
    
    # Simple fix for .setExecutive(executive) -> .setExecutiveId(executiveId)
    content = re.sub(r'\.setExecutive\(.*?executive.*?\)', '.setExecutiveId(executiveId)', content)
    content = re.sub(r'\.setExecutive\(.*?\)', '.setExecutiveId(executiveId)', content)

    # Replace occurrences of executive.getFullName() with a placeholder or mock
    content = re.sub(r'executive\.getFullName\(\)', '"Mock Name"', content)
    content = re.sub(r'executive\.getVehicleNumber\(\)', '"Mock Vehicle"', content)
    content = re.sub(r'executive\.getPhoneNumber\(\)', '"Mock Phone"', content)

    with open(filepath, 'w') as f:
        f.write(content)

for root, dirs, files in os.walk(dir_path):
    for file in files:
        if file.endswith(".java"):
            process_file(os.path.join(root, file))

